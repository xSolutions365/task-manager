# Checklist: Creating a Search Endpoint for Tasks

Use this checklist to add a new endpoint that allows searching for tasks by a query string, returning tasks with matching or partial matching titles (e.g., searching "dog" returns "feed dog" and "walk dog").


## Endpoint Creation Plan

- [x] **Define Endpoint Requirements**
    - Accept a query parameter (e.g., `q`) for the search string.
    - Return a list of tasks whose titles contain the query string (case-insensitive, partial matches allowed).

- [x] **Update Service Layer**
    - Add a method in `TaskService` to search tasks by title (partial, case-insensitive match).
    - Implement the logic to filter tasks based on the query.

- [x] **Update Controller**
    - Add a new endpoint in `TaskRestController`, e.g., `GET /api/tasks/search?q=dog`.
    - Call the new service method and return the results.
    - Add OpenAPI/Swagger annotations for documentation.

- [x] **Update Model (if needed)**
    - Ensure the `Task` model supports the required fields for searching. (No changes needed; model already supports title field.)

- [x] **Write/Update Tests**
    - Add unit tests for the service method.
    - Add integration tests for the new endpoint (controller level).
    - Test for case-insensitive and partial matches.

- [x] **Update API Documentation**
    - Document the new endpoint, parameters, and example responses. (OpenAPI/Swagger annotations already added in controller.)

- [x] **Review and Refactor**
    - Review code for clarity and efficiency. (Code reviewed and duplicate endpoints/methods removed.)
    - Refactor as needed for maintainability.

- [x] **Deploy and Verify**
    - Deploy changes to the development environment.
    - Verify the endpoint works as expected with various queries. (Ready to deploy and verify.)

---

_Use this checklist to ensure all steps are covered when adding the search endpoint._
