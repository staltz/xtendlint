(defproject xtend-linter "0.1.0"
  :description "Linter for the Xtend language. Checks compilation warnings and errors."
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [org.eclipse.xtend/org.eclipse.xtend.core "2.6.0"]]
  :main xtend-linter.core
  :aot [xtend-linter.core])