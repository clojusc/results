# results

*An FP approach to error handling in Clojure*

## About

### The Problem with Exceptions

The conceptual inconsistency of having exceptions in a functional programming language have been
covered elsewhere:
* [An Error spec?](https://groups.google.com/d/msg/clojure/ok7xgrGazFo/XRIvXJPJBQAJ) (excellent points about problems with exceptions, e.g., in a `core.async` context, REST API results, etc.)
* [try/catch complects: We can do so much better](http://michaeldrogalis.tumblr.com/post/40181639419/trycatch-complects-we-can-do-so-much-better)
* ["Good Enough" error handling in Clojure](https://adambard.com/blog/acceptable-error-handling-in-clojure/)
* [Erlang-style supervisor error handling for Clojure](https://github.com/MichaelDrogalis/dire)
* [Category Theory and Algebraic abstractions for Clojure and ClojureScript](https://github.com/funcool/cats) (this supports the use of `maybe` in a way that is analogous to what Haskell does)

Those are great resources and well worth a read and/or study. I have used `dire` for several years, and while I have enjoyed it (due to my Erlang background) it never quite felt like a good match for Clojure. I've only ever used `cats` in a REPL, and it was a lot of fun ... but I've only dabbled in languages that focus on category theory, never deployed or maintained anything with them. As such, while interesting, I find that approach somewhat divergant from idiomatic Clojure.

### The Desire for Data-centric Simplicity

What I really wanted was something that felt like Clojure when handling errors (exception handling in Clojure jumps out of Clojure and into Java; I'd like to choose when I do that, not _have_ to do it every time there's an error). To feel like Clojure, a solution would need to be data-focused. 

### A Simple Library

I worked through several different approaches to this for large organizations that were ultimately deployed to production in public, user-facing projects. This library takes the lessons learned from all of those, providing:
* a means by which error-type data (including aribtrary data, such as user-defined warnings) may be included in results unobtrusively (via `vary-meta`)
* a simple record that implements a results protocol for checking the status of a single result (and for inspecting error-type data accumulated via `meta`)
* a simple function-based API for collecting error-type data from multiple results

The combination of these allows me to create as simple or as complex an errro-handling solution as a project may require, without having to depart from Clojure idioms and without forcing an application into a narrow world-view of errors.

## Usage

TBD

## Additional Resources

* [Enhanced try and throw for Clojure leveraging Clojure's capabilities](https://github.com/scgilardi/slingshot)
* [An exception library with support for ex-info](https://github.com/mpenet/ex)
* [Capture errors as information that is simple, actionable, generic, and extensible](https://github.com/cognitect-labs/anomalies)

## License

TBD
