#Golden Method Section Search Algorithm script for R
#@Alexander MacKenzie

#Given f unimodal with minimum in [a,b]

#Setting variables
k=40	#Iterations
a=0	#Interval
b=1
g=((sqrt(5)-1)/2)
FofX <- function(x){
	x^6-11*(x^3)+17*(x^2)-7*x+1 #Function f
}

#Graph of F(x)
x=seq(-3,3, by=1/32)
plot(x, FofX(x), xlim = c(a-1, b+1), ylim = c(a-1, b+1), main='Golden Section Search', ylab='y', type="l", col="red")

#Setting table vectors
A=c() 
x1=c()
x2=c()
B=c()

#Golden Method Section Search
for(i in 0:k){
	#x1
	x=a+(1-g)*(b-a)
	x1=c(x1,x) #Appending to table
	xa=FofX(x)

	#x2
	x=a+g*(b-a)
	x2=c(x2,x) #Appending to table
	xb=FofX(x)
	
	#Appending to table
	A=c(A,a) 
	B=c(B,b)

	if(xa < xb){
		b=a+g*(b-a)
	}
	else{
		a=a+(1-g)*(b-a)
	}
}

#Results
table <- data.frame(A,x1,x2,B)
print(table)
cat('Therefore, after ',k,' steps, the minimum is between ',A[k],' and ',B[k])

