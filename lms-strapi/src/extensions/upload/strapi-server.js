'use strict';

const fs = require('fs');

/**
 * Strapi 5 forces every file uploaded through the REST API (/api/upload)
 * into the "API Uploads" folder and strips `fileInfo.folder` from the request.
 *
 * This extension wraps the upload plugin's content-api controller so that,
 * when the client sends `fileInfo = {"folder": <id>}`, the uploaded file(s)
 * are moved into that Media Library folder (e.g. "LMS Content") after upload.
 */
module.exports = (plugin) => {
  const originalContentApi = plugin.controllers['content-api'];

  // In Strapi 5 the controller is a factory: ({ strapi }) => ({ upload, uploadFiles, ... })
  plugin.controllers['content-api'] = (deps) => {
    const controller =
      typeof originalContentApi === 'function' ? originalContentApi(deps) : originalContentApi;

    const originalUpload = controller.upload;

    controller.upload = async function (ctx) {
      const folderId = getRequestedFolderId(ctx);
      const isNewUpload = !ctx.query?.id;

      await originalUpload.call(controller, ctx);

      if (!isNewUpload || !folderId || ctx.status >= 400) return;

      const folder = await strapi.db
        .query('plugin::upload.folder')
        .findOne({ where: { id: folderId } });

      if (!folder) {
        strapi.log.warn(`[upload] Folder ${folderId} not found, file left in API Uploads`);
        return;
      }

      const files = Array.isArray(ctx.body) ? ctx.body : [ctx.body];
      for (const file of files) {
        if (!file?.id) continue;

        await strapi.db.query('plugin::upload.file').update({
          where: { id: file.id },
          data: { folder: folder.id, folderPath: folder.path },
        });

        strapi.log.info(`[upload] Moved file ${file.id} to folder "${folder.name}"`);
      }
    };

    return controller;
  };

  return plugin;
};

// Reads the folder id from the multipart `fileInfo` part (sent as a JSON string)
function getRequestedFolderId(ctx) {
  let fileInfo = ctx.request.body?.fileInfo;

  // Clients like Spring RestTemplate send `fileInfo` with a Content-Type header
  // (text/plain), so koa-body stores it as an uploaded file instead of a body field
  const fileInfoPart = ctx.request.files?.fileInfo;
  if (fileInfo === undefined && fileInfoPart?.filepath) {
    try {
      fileInfo = fs.readFileSync(fileInfoPart.filepath, 'utf8');
    } catch {
      fileInfo = null;
    }
    fs.rm(fileInfoPart.filepath, { force: true }, () => {});
    delete ctx.request.files.fileInfo;
  }

  if (typeof fileInfo === 'string') {
    try {
      fileInfo = JSON.parse(fileInfo);
    } catch {
      return null;
    }
  }

  if (Array.isArray(fileInfo)) fileInfo = fileInfo[0];

  const folderId = Number(fileInfo?.folder);
  return Number.isInteger(folderId) && folderId > 0 ? folderId : null;
}
