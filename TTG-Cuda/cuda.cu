
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <sys/types.h>
#include <sys/times.h>
#include <sys/time.h>
#include <time.h>

/* Program Parameters */
#define MAXN 15000  /* Max value of N */
#define TILE_WIDTH 32  /* Width of each block */
int N;  /* Matrix size */

/* Matrices */
float overall;
char buffer[10000];
  char *pbuff;
  int *classIdArray = (int *)malloc(sizeof(int)*26);
  int *groupIdArray = (int *)malloc(sizeof(int)*26);
  int *roomIdArray =(int *) malloc(sizeof(int)*26);
  int *roomSizeAsArray = (int *)malloc(sizeof(int)*5);
  int *groupSizeAsArray = (int *)malloc(sizeof(int)*11);
  int *timeSlotIdArray = (int *)malloc(sizeof(int)*26);
  int *profIdArray = (int *)malloc(sizeof(int)*26);
  int *clashes=(int *)calloc(26*26,sizeof(int));

int   *dclassIdArray,*dgroupIdArray,*droomIdArray,*droomSizeAsArray,*dgroupSizeAsArray,*dtimeSlotIdArray,*dprofIdArray,*dclashes;

/* junk */
#define randm() 4|2[uid]&3


/* returns a seed for srand based on the time */
unsigned int time_seed() {
  struct timeval t;
  struct timezone tzdummy;

  gettimeofday(&t, &tzdummy);
  return (unsigned int)(t.tv_usec);
}

//Kernel to calculate fitness

__global__ void calculateFitness(int *classIds, int *roomIds, int *roomCapacities, int *groupIds, int *groupSizes, int *timeSlotIds, int *profIds, int *clashes)
{
	int i = threadIdx.x;
	int j = blockIdx.x ;
	
	if(classIds[i]==classIds[j] && roomCapacities[classIds[i]]<groupSizes[groupIds[classIds[i]]])
		clashes[i*j+i]++;
		
	if (roomIds[classIds[j]] == roomIds[classIds[i]] && timeSlotIds[classIds[i]] == timeSlotIds[classIds[j]]
			&& classIds[i] != classIds[j]) {
		clashes[i*j+i]++;
	}

	if (profIds[classIds[j]] == profIds[classIds[j]] && timeSlotIds[classIds[i]] == timeSlotIds[classIds[j]]
			&& classIds[i] != classIds[j]) {
		clashes[i*j+i]++;
	}
}

//Main function to execute fitness function 

int main(int argc, char **argv) {
  /* Timing variables */
  struct timeval etstart, etstop;  /* Elapsed times using gettimeofday() */
  struct timezone tzdummy;
  clock_t etstart2, etstop2;  /* Elapsed times using times() */
  unsigned long long usecstart, usecstop;
  struct tms cputstart, cputstop;  /* CPU times for my processes */
  /* Process program parameters */
  srand(time_seed());
  int size = N * N * sizeof( int );

  
  /* --------Reading frm intermediate data ----------*/

  
   FILE *fp;
  int i=0;
  fp=fopen("file.txt","r");
  
  while (1) {
    if (!fgets(buffer, sizeof buffer, fp)||feof(fp)) break;
    pbuff = buffer;
    i=0;
    /* copying from intermediate file*/
    while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      classIdArray[i] = strtol(pbuff, &pbuff, 10);
      //printf(" %d", classIdArray[i]);
      i++;
    }
    //printf("\n");
    i=0;
    //printf("groupIdArray\n");
    fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
    while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      groupIdArray[i] = strtol(pbuff, &pbuff, 10);
      //printf(" %d", groupIdArray[i]);
      i++;
    }

    fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
    i=0;

    while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      roomIdArray[i] = strtol(pbuff, &pbuff, 10);
      //printf(" %d", roomIdArray[i]);
      i++;
    }

    fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
    i=0;

    while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      roomSizeAsArray[i] = strtol(pbuff, &pbuff, 10);
      //printf(" %d", roomSizeAsArray[i]);
      i++;
    }

    fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
    i=0;
    while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      groupSizeAsArray[i] = strtol(pbuff, &pbuff, 10);
       i++;
    }
    i=0;
     fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
     while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      timeSlotIdArray[i] = strtol(pbuff, &pbuff, 10);
       i++;
    }
     fgets(buffer, sizeof buffer, fp);
    pbuff = buffer;
    i=0;
     while (1) {
      if (*pbuff == 13 || *pbuff == 10) break;
      profIdArray[i] = strtol(pbuff, &pbuff, 10);
       i++;
    }
   
  

  
  /* */

 
  cudaMalloc( (void **) &dclashes, sizeof(int)*26*26 );
  cudaMalloc( (void **) &dclassIdArray, sizeof(int)*26 );
  cudaMalloc( (void **) &dgroupIdArray, sizeof(int)*26 );
  cudaMalloc( (void **) &droomIdArray, sizeof(int)*26 );
  cudaMalloc( (void **) &droomSizeAsArray, sizeof(int)*5 );
  cudaMalloc( (void **) &dgroupSizeAsArray, sizeof(int)*11 );
  cudaMalloc( (void **) &dtimeSlotIdArray, sizeof(int)*26 );
  cudaMalloc( (void **) &dprofIdArray, sizeof(int)*26 );
  dim3 dimGrid(26,1,1);
  dim3 dimBlock(26, 1,1);
  cudaMemcpy( dclassIdArray, classIdArray, sizeof(int)*26, cudaMemcpyHostToDevice );
  cudaMemcpy( dgroupIdArray, groupIdArray, sizeof(int)*26, cudaMemcpyHostToDevice );
  cudaMemcpy( droomIdArray, roomIdArray, sizeof(int)*26, cudaMemcpyHostToDevice );
  cudaMemcpy( droomSizeAsArray, roomSizeAsArray, sizeof(int)*5, cudaMemcpyHostToDevice );
  cudaMemcpy( dgroupSizeAsArray, groupSizeAsArray, sizeof(int)*11, cudaMemcpyHostToDevice );
  cudaMemcpy( dtimeSlotIdArray, timeSlotIdArray, sizeof(int)*26, cudaMemcpyHostToDevice );
  cudaMemcpy( dprofIdArray, profIdArray, sizeof(int)*26, cudaMemcpyHostToDevice );
	
	  /* Start Clock */
  gettimeofday(&etstart, &tzdummy);
  etstart2 = times(&cputstart);

  calculateFitness<<<dimGrid,dimBlock>>>(dclassIdArray,droomIdArray,droomSizeAsArray,dgroupIdArray,dgroupSizeAsArray,dtimeSlotIdArray,dprofIdArray,dclashes);

	
  /* Stop Clock */
  gettimeofday(&etstop, &tzdummy);
  etstop2 = times(&cputstop);


  cudaMemcpy( clashes, dclashes, sizeof(int)*26*26, cudaMemcpyDeviceToHost );



  usecstart = (unsigned long long)etstart.tv_sec * 1000000 + etstart.tv_usec;
  usecstop = (unsigned long long)etstop.tv_sec * 1000000 + etstop.tv_usec;

  int x=0;
  int y=26*26;
  int z=0;
  for(x=0;x<y;x++)
  {
	  z=z+clashes[x];
  }

  
  cudaFree(dclashes);
  cudaFree(dclassIdArray);
  cudaFree(dgroupIdArray);
  cudaFree(droomIdArray);
  cudaFree(droomSizeAsArray);
  cudaFree(dgroupSizeAsArray);
  cudaFree(dtimeSlotIdArray);
  cudaFree(dprofIdArray);
  overall=overall+(float)(usecstop - usecstart)/(float)1000;
  
  
}
free(classIdArray);
  free(groupIdArray);
  free(roomIdArray);
  free(roomSizeAsArray);
  free(clashes);
  free(groupSizeAsArray);
  free(timeSlotIdArray);
  free(profIdArray); 

  printf("\nElapsed time = %g ms.\n",overall);
  exit(0);
}


