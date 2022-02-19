(defun factorial(number)
    (if (< number 0)
        "MATH ERROR"
        (if (= number 0) 1
        (* number (factorial (- number 1))))))

(write (factorial -1))