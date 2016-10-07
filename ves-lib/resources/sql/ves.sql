-- name: select-all-vertex-types
SELECT * FROM vertex_types WHERE deleted_at IS NULL

-- name: select-all-edge-types
SELECT * FROM edge_types WHERE deleted_at IS NULL
