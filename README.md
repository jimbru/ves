# ves

Vertex-Edge Store. A library (and supporting command line tool) for storing
loosely-schemaed graph-oriented data in Postgres. Cribbed from Facebook's
[fbobj/assoc framework][1].

[1]: https://research.facebook.com/publications/tao-facebook-s-distributed-data-store-for-the-social-graph/

## Installation

Add to your Clojure project file:

```clojure
[ves "0.1.0"]
```

## Usage

Use the command line tool to generate migrations for schema changes:

``` bash
java -jar ves-tool-0.1.0-standalone.jar [args]
```

Call the library from your Clojure code to transact with the database:

``` clojure
(require '[ves.core :refer :all])
```

Ves uses the
[totally excellent conf library](https://github.com/jimbru/conf) to handle
configuration. You'll need to make sure the `:database-url` key is set so that
ves knows where to find Postgres.

## License

Copyright © 2016 Jim Brusstar

Distributed under the MIT License.
