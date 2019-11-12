package illumi.code.ddd.model;

public class DDDIssue {
	
	private DDDIssueType type;
	
	private String description;

	public DDDIssue(DDDIssueType type, String description) {
		super();
		this.type = type;
		this.description = description;
	}

	public DDDIssueType getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}
}
