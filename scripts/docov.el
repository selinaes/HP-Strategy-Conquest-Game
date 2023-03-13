;;(setq gradle-executable-path "./gradlew")
;;(dcoverage-generate-and-save "coverage.txt")
;;(while (not dcoverage-save-done)
;;  (sleep-for 1))

(dcoverage-load-default-file)
(dcoverage-show-coverage-results)
(write-file "coverage.txt")