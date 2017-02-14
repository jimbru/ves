-- name: select-all-vertex-types
SELECT * FROM vertex_types WHERE deleted_at IS NULL

-- name: select-all-edge-types
SELECT * FROM edge_types WHERE deleted_at IS NULL

-- name: vertex-get
SELECT * FROM vertices WHERE id = :id AND deleted_at IS NULL

-- name: vertex-create<!
INSERT INTO vertices (type, data, created_at, updated_at)
VALUES (:type, :data, :now, :now)

-- name: vertex-update!
UPDATE vertices SET data = :data, updated_at = :now
WHERE id = :id AND deleted_at IS NULL

-- name: vertex-delete!
UPDATE vertices SET deleted_at = :now WHERE id = :id AND deleted_at IS NULL

-- name: edge-get
SELECT * FROM edges
WHERE id1 = :id1 AND type = :type AND id2 = :id2 AND deleted_at IS NULL

-- name: edge-count
SELECT COUNT(*) FROM edges
WHERE id1 = :id1 AND type = :type AND deleted_at IS NULL

-- name: edge-range
SELECT * FROM edges
WHERE id1 = :id1 AND type = :type AND deleted_at IS NULL
ORDER BY id2 DESC
LIMIT :limit

-- name: edge-range-offset
SELECT * FROM edges
WHERE id1 = :id1 AND type = :type AND id2 <= :id2 AND deleted_at IS NULL
ORDER BY id2 DESC
LIMIT :limit

-- name: edge-create<!
INSERT INTO edges (id1, type, id2, data, created_at, updated_at)
VALUES (:id1, :type, :id2, :data, :now, :now)

-- name: edge-update!
UPDATE edges SET data = :data, updated_at = :now
WHERE id1 = :id1 AND type = :type AND id2 = :id2 AND deleted_at IS NULL

-- name: edge-delete!
UPDATE edges SET deleted_at = :now
WHERE id1 = :id1 AND type = :type AND id2 = :id2 AND deleted_at IS NULL

-- name: schema-vertex-type-create<!
INSERT INTO vertex_types (name, created_at, updated_at)
VALUES (:name, :now, :now)

-- name: schema-edge-type-create<!
INSERT INTO edge_types (name, created_at, updated_at)
VALUES (:name, :now, :now)
