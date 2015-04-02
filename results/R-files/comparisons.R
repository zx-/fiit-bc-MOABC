## DOMINATION AND NON DOMINATION COMPARISONS IN MOABC ##

library(ggplot2)

#comaprisons

files = list.files(path="./../comparisons", pattern="*.fasta", full.names=T, recursive=FALSE)
print("data read")

for(i in 1:length(files)){
  
  comparisons = read.table(file=files[i],header=T, sep=",")
  print(files[i])
  
  jpeg(
    file=paste("./../graphs/comparisons/",basename(files[i]),".jpg",sep =""),
    width = 1500, height = 1000, units = "px", pointsize = 12,
    quality = 225)
  
  numberOfphases=length(unique(comparisons$var))/2
  
  print(ggplot(
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
  )
  dev.off()  
  
}



## LEvys##

#read data
files = list.files(path="./../levys", pattern="*.fasta", full.names=T, recursive=FALSE)
print("data read")

for(i in 1:length(files)){
  
  
  levys = read.table(file=files[i],header=T, sep=",")
  print(files[i])


  jpeg(file=paste("./../graphs/levys/",basename(files[i]),".jpg",sep =""),
     width = 1500, height = 1000, units = "px", pointsize = 12,
     quality = 225)

  y = levys$NotImproved / ( levys$Improved + levys$NotImproved)
  
  plot(1:length(levys$NotImproved), y,type = "l")

  dev.off()

}

## front size##

#read data
files = list.files(path="./../paretoFrontSize", pattern="*.fasta", full.names=T, recursive=FALSE)
print("data read")


for(i in 1:length(files)){
  
  
  front = read.table(file=files[i],header=T, sep=",")
  print(files[i])
  
  
  jpeg(file=paste("./../graphs/paretoFrontSize/",basename(files[i]),".jpg",sep =""),
       width = 1500, height = 1000, units = "px", pointsize = 12,
       quality = 225)
  
  
  plot(1:length(front$size), front$size,type = "l")
  
  dev.off()
  
}


## LEvys##

#read data
files = list.files(path="./../fitnesses", pattern="*.fasta", full.names=T, recursive=FALSE)
print("data read")

for( i in 1:length(files)){
  
  fitness = read.table(file=files[i],header=T, sep=",")
  minLength=aggregate(fitness$length, list(fitness$iteration),min)
  maxLength=aggregate(fitness$length, list(fitness$iteration),max)
  avgLength=aggregate(fitness$length, list(fitness$iteration),mean)
  print(basename(files[i]))
  
  jpeg(file=paste("./../graphs/fitness/",basename(files[i]),"-length.jpg",sep =""),
       width = 1500, height = 1000, units = "px", pointsize = 12,
       quality = 225)
  
  print(ggplot(data) + 
    geom_ribbon(aes(minLength$Group.1,ymin=minLength$x,ymax=maxLength$x),color="yellow",alpha=0.5) +
    geom_line(aes(minLength$Group.1,avgLength$x),color="black"))
  
  dev.off()
  
  minSupport=aggregate(fitness$support, list(fitness$iteration),min)
  maxSupport=aggregate(fitness$support, list(fitness$iteration),max)  
  meanSupport=aggregate(fitness$support, list(fitness$iteration),mean)
  
  jpeg(file=paste("./../graphs/fitness/",basename(files[i]),"-support.jpg",sep =""),
       width = 1500, height = 1000, units = "px", pointsize = 12,
       quality = 225)
  
  print(ggplot(data) + 
    geom_ribbon(aes(minSupport$Group.1,ymin=minSupport$x,ymax=maxSupport$x),color="yellow",alpha=0.5) +
    geom_line(aes(meanSupport$Group.1,meanSupport$x),color="black"))
  
  dev.off()
  
  minsimilarity=aggregate(fitness$similarity, list(fitness$iteration),min)
  maxsimilarity=aggregate(fitness$similarity, list(fitness$iteration),max)  
  meansimilarity=aggregate(fitness$similarity, list(fitness$iteration),mean)
  
  jpeg(file=paste("./../graphs/fitness/",basename(files[i]),"-sim.jpg",sep =""),
       width = 1500, height = 1000, units = "px", pointsize = 12,
       quality = 225)
  
  print(ggplot(data) + 
    geom_ribbon(aes(minsimilarity$Group.1,ymin=minsimilarity$x,ymax=maxsimilarity$x),color="yellow",alpha=0.5) +
    geom_line(aes(meansimilarity$Group.1,meansimilarity$x),color="black"))
  
  dev.off()
  
}