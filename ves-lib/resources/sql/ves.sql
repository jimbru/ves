-- name: select-all-vertex-types
SELECT * FROM vertex_types WHERE deleted_at IS NULL

-- name: select-all-edge-types
SELECT * FROM edge_types WHERE deleted_at IS NULL

-- name: vertex-create<!
INSERT INTO vertices (type, data, created_at, updated_at)
VALUES (:type, :data, :now, :now);

-- name: vertex-get
SELECT * FROM vertices WHERE id = :id AND deleted_at IS NULL

-- name: vertex-update!
UPDATE vertices SET data = :data, updated_at = :now
WHERE id = :id AND deleted_at IS NULL

-- name: vertex-delete!
UPDATE vertices SET deleted_at = :now WHERE id = :id AND deleted_at IS NULL
