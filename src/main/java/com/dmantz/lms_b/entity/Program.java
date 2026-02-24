package com.dmantz.lms_b.entity;

import com.dmantz.lms_b.entity.base.AuditFields;

import jakarta.persistence.*;

@Entity
@Table(name = "program")
public class Program extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "program_id", nullable = false, unique = true)
	private String programId;

	@Column(name = "program_title", nullable = false)
	private String programTitle;

	@Column(name = "description")
	private String description;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ProgramStatus status = ProgramStatus.ACTIVE;

	@Column(name = "duration_in_months")
	private Integer durationInMonths;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id", nullable = false)
	private Provider provider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProgramStatus getStatus() {
		return status;
	}

	public void setStatus(ProgramStatus status) {
		this.status = status;
	}

	public Integer getDurationInMonths() {
		return durationInMonths;
	}

	public void setDurationInMonths(Integer durationInMonths) {
		this.durationInMonths = durationInMonths;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public String toString() {
		return "Program{" + "id=" + id + ", programId='" + programId + '\'' + ", programTitle='" + programTitle + '\''
				+ ", description='" + description + '\'' + ", status=" + status + ", durationInMonths="
				+ durationInMonths + ", provider=" + provider + '}';
	}
}
