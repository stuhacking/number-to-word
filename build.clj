(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'stuhacking/num2word)
(def version "0.2.0")
(def class-dir "target/classes")
(def uber-file (format "target/%s-%s.jar" (name lib) version))

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (println "Copying sources...")
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (println "Compiling clojure...")
  (b/compile-clj {:basis @basis
                  :ns-compile '[num2word.main]
                  :class-dir class-dir})
  (println "Creating uberjar...")
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'num2word.main}))
