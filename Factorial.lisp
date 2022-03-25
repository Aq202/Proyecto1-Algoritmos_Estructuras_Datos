(defun factorial(number)
    (cond ((< number 0) "MATH ERROR")
          ((= number 0) 1)
	  ((* number (factorial (- number 1))))
    )
)

(write (factorial 4))