(setq previous 0)
(defun fibonacci(num)
    (write previous)
    (setq new (+ previous num))
    (setq previous num)
    (cond ((< new 10)
        (fibonacci new)))
)
(fibonacci 1)