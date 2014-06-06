(ns xtend-linter.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class)
  (:import (org.eclipse.xtend.core XtendInjectorSingleton XtendStandaloneSetup)
           (org.eclipse.xtext.resource XtextResourceSet)
           (org.eclipse.emf.ecore.resource Resource)
           (org.eclipse.emf.ecore.resource.impl ResourceImpl)
           (org.eclipse.emf.common.util URI EList)
           (java.io FileInputStream File)
           (org.eclipse.xtext.diagnostics Diagnostic)))

(defn printDiagnostic [diag]
  (println (str "L" (. diag getLine) ": " (. diag getMessage))))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args nil)
        path (first arguments)
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
        (printDiagnostic diagnostic)))))
