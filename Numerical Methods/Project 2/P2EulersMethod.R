#Euler's Method for Initial Value Problems
#@Alexander MacKenzie

#to use: 
#	edit functions FofX and FPrimeofX to match your problem
#	run the whole script
#For results with comparison if F(x) can be found
#	execute EulersMethodCompare(initial value, number of steps, lower t bound, upper t bound)
#For results with approximation only
#	execute EulersMethod(initial value, number of steps, lower t bound, upper t bound)


#setting functions
FPrimeofX = function(t, y){
	t*y+t^3
}
FofX = function(t){
	(3*exp(1)^((t^2)/2))-(t^2)-2
}


#Euler's Method
EulerStep = function(t,w,h){
	w+h*FPrimeofX(t,w)	#wi+1=wi+hf(ti,wi)
}
EulersMethodCompare = function(y,n,inter1,inter2) {
	t=c()
	w=c(y)	#w0=y0
	y=c()
	error=c()
	h=(inter2-inter1)/n	#finding step size
	
	for(i in 0:n) t=c(t,h*i)	#setting t to a vector
	for(i in 1:n){
		w=c(w,EulerStep(t[i],w[i],h))	#Euler Step
		
	}
	for(i in 0:n+1){
		y=c(y,FofX(t[i]))	#setting y to a vector
		error=c(error,abs(y[i]-w[i]))	#setting error to a vector
	}

	plot(t, y, main="Euler's Method", ylab="y", xlab="t", col="red", type="l")	#graph
	points(t, w, col="blue")
	legend("topleft", legend=c("y Exact", "y' Approximated"), col=c("red", "blue"), lty=1:3, cex=1)

	table <- data.frame(t,w,y,error)	#table
	print(table)
}
EulersMethod = function(y,n,inter1,inter2) {
	t=c()
	w=c(y)	#w0=y0

	h=(inter2-inter1)/n	#finding step size
	
	for(i in 0:n) t=c(t,h*i)	#setting t to a vector
	for(i in 1:n){
		w=c(w,EulerStep(t[i],w[i],h))	#Euler Step
		
	}

	plot(t, w, main="Euler's Method", ylab="y", xlab="t", col="blue", type="p")	#graph
	legend("topleft", legend=c("y' Approximated"), col=c("blue"), lty=3, cex=1)

	table <- data.frame(t,w)	#table
	print(table)
}
