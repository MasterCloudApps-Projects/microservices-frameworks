package es.codeurjc.orderservice.query.queries;

public class FindOrderByIdQuery {
	private String id;

    public FindOrderByIdQuery(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
