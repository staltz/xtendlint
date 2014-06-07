(ns xtend-linter.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string])
  (:gen-class)
  (:import (org.eclipse.xtend.core XtendInjectorSingleton XtendStandaloneSetup)
           (org.eclipse.xtext.resource XtextResourceSet)
           (org.eclipse.emf.ecore.resource Resource)
           (org.eclipse.emf.ecore.resource.impl ResourceImpl)
           (org.eclipse.emf.common.util URI EList)
           (java.io FileInputStream File)
           (org.eclipse.xtext.diagnostics Diagnostic)))

(def cli-options
  [["-h" "--help" "Shows this usage guide"]
   ["-v" "--version" "Displays the version number"]])

(defn usage [options-summary]
  (->> ["Linter for the Xtend language. Checks for syntax errors and warnings."
        ""
        "Usage: xtendlint [options] src"
        ""
        "src: path to the Xtend file to be linted"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while attempting to lint:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn printDiagnostic [diag]
  (println (str "L" (. diag getLine) ": " (. diag getMessage))))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (:version options) (exit 0 "0.1.0")
      (not= (count arguments) 1) (exit 1 (usage summary)))
    (let [path (first arguments)
          injector (. (XtendStandaloneSetup.) createInjectorAndDoEMFRegistration)
          resourceSet (. injector getInstance XtextResourceSet)
          resource (. resourceSet createResource (URI/createFileURI path))
          in (FileInputStream. (File. path))]
      (try
        (. resource load in nil)
        (catch Exception e (throw e))
        (finally (. in close)))
      (let [errors (. resource getErrors)
            warnings (. resource getWarnings)]
        (doseq [diagnostic (seq errors)]
          (printDiagnostic diagnostic))))))
