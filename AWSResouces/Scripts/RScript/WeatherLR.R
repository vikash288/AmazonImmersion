#--------------------Load data from Redshift-------------------#
#--------------------------------------------------------------#

#setting up connection with Redshift
#install.packages("rJava")
#install.packages("RJDBC")
#install.packages("devtools")

library(rJava)
library(RJDBC)
devtools::install_github("pingles/redshift-r",force = TRUE)
#devtools::install_github("pingles/redshift-r")
require(redshift)
args <- commandArgs(trailingOnly=TRUE)
conn <- redshift.connect(paste("jdbc:postgresql://",args[4],sep = ""), username=args[2], password=args[3])



tables <- redshift.tables(conn)
#tables

#reading training data and testing data from Redshift
train <- redshift.query(conn, "select * from weather_storm_data")
test<- redshift.query(conn, "select * from weather_data_incremental")

#--------------------Logistic Regression-------------------#
#----------------------------------------------------------#

#Fit Logistic Regression
model <- glm(storm ~ tmax+ tmin+ windspeed+ rainfall, family=binomial(link='logit'), data=train)

#Viewing results for Interpretation
#summary(model)

#Assessing the predictive ability of the model
predicted.results <- predict(model, newdata= test, type= 'response')
predicted.results <- ifelse(predicted.results> 0.5,1,0)
final <- cbind(test, predicted.results)


#--------------------Saving file locally-------------------#
#----------------------------------------------------------#


#write a csv and load to s3
write.csv(final, file = paste(args[1],"//Output//prediction.csv",sep = ""))


