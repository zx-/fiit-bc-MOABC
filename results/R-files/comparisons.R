## DOMINATION AND NON DOMINATION COMPARISONS IN MOABC ##

library(ggplot2)

#read data
comparisons = read.table(file="c:\\Users\\Gamer\\Documents\\NetBeansProjects\\BC_DNA\\results\\comparisons-1",header=T, sep=",")
print("data read")

#comparisons$var = factor(comparisons$var, levels = c("Worker Dominated","Onlooker Dominated","Worker non-Dominated","Onlooker non-Dominated"))

jpeg(
  file="c:\\Users\\Gamer\\Documents\\NetBeansProjects\\BC_DNA\\results\\comparisons-1.jpg",
  width = 1500, height = 1000, units = "px", pointsize = 12,
  quality = 225)

  numberOfphases=length(unique(comparisons$var))/2

  ggplot(
    comparisons,
    aes(
      x=t,
      y=count,
      group=var,
      fill=var,
      )
    ) +  
    geom_area( position="fill") +
    scale_fill_manual(values=as.vector(rbind(rgb(0, (1:numberOfphases)/numberOfphases, 0),rgb((1:numberOfphases)/numberOfphases, 0, 0))))

dev.off()

## DOMINATION AND NON DOMINATION COMPARISONS IN MOABC ##

#read data
levys = read.table(file="c:\\Users\\Gamer\\Documents\\NetBeansProjects\\BC_DNA\\results\\levys-1",header=T, sep=",")
print("data read")


jpeg(file="c:\\Users\\Gamer\\Documents\\NetBeansProjects\\BC_DNA\\results\\levy-1.jpg",
     width = 1500, height = 1000, units = "px", pointsize = 12,
     quality = 225)

  y = levys$NotImproved / ( levys$Improved + levys$NotImproved)
  
  plot(1:length(levys$NotImproved), y,type = "l")

dev.off()