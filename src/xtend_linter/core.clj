(ns xtend-linter.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class)
  (:import (org.eclipse.xtend.core XtendInjectorSingleton))
  (:import (org.eclipse.xtend.core.compiler.batch XtendBatchCompiler)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args nil)
        injector XtendInjectorSingleton/INJECTOR
        compiler (. injector getInstance XtendBatchCompiler)]
    (. compiler setVerbose true)
    (. compiler setOutputPath "/path/to/project/.trashdir")
    (. compiler setSourcePath "/path/to/project/src")
    (println compiler)
    (. compiler compile)))

(-main)

; This is what I used for testing out compiler.batch.Main
; inside the -main
; (Main/main (into-array arguments))

