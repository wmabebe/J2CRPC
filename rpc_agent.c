#include <stdio.h> 
#include <stdlib.h> 
#include <pthread.h>
#include <unistd.h>
#include <string.h> 
#include <sys/types.h> 
#include <sys/socket.h> 
#include <arpa/inet.h> 
#include <netinet/in.h>
#include <errno.h> 
#include <netdb.h> 
#include <string.h>
#include <sys/utsname.h>
#include "data_structures.h"


/*
 * GLobals and Constants
 */

int PORT =  8080;
char GLOS[100] = "getLocalOS";
char GLT[100] = "getLocalTime";
#define MAX 1024
#define SA struct sockaddr

/*
 * Function declarations
 */

void getLocalOS(GET_LOCAL_OS *);
void getLocalTime(GET_LOCAL_TIME *);
void* cmdHandler(void*);
void cmdAgent();

/*
 * Function: getLocalOS: OS,valid
 * -----------------------------
 * Get's underlying Operating System name and copies it into the char array 'OS'
 * If OS name cannot be determined, it sets the flag valid to 0
 *
 */
void getLocalOS(GET_LOCAL_OS *ds){
	struct utsname uts;
	strcpy(ds->OS,uname(&uts) ? "Unknown!" : uts.sysname);
	ds->valid = strncmp(ds->OS,"Unknown!",8) == 0 ? 0 : 1;
}
/* Function: getLocalTime: t, valid
 * ---------------------------------
 * Set's the local time in seconds to the value t.
 * Set's flag valid to 0 if time cannot be determined.
 *
 */
 void getLocalTime(GET_LOCAL_TIME *ds){
	time_t seconds;
    ds->time = (unsigned) time(&seconds);
	ds->valid = ds->time > 0 ? 1 : 0;
 }


/*
 * Function listener: sockfd
 * --------------------------
 *  => Given a socket descriptor, listen for TCP packets and decode commands.
 *  => Expects remote commands placed inside buffer[].
 *
 *  => For the sake of demo purposes, the commands have not been parsed. Instead,
 *     they are displayed sequentially as this program intermittently replies with
 *     values to the remote program.

 */

void *cmdHandler(void* value) 
{ 
    int sockfd = *((int*) value);
    char buff[MAX];
    char command[100];
    char paramSize[4];
    char null[1] = {'\0'}; 
    // Read command from Manager and respond
    bzero(buff, MAX); 
    read(sockfd, buff, sizeof(buff));
    if (sizeof(buff) >= 104){
		strncpy(command,buff,100);
		strncpy(paramSize,&buff[100],4);
		
		if (strcmp(command,GLT) == 0){
			if (sizeof(buff) < 108){
				printf("Command %s buffer too short!\n",strcat(command,null));
				return NULL;
			}
			printf("Command: %s\n", strcat(command,null)); 
			GET_LOCAL_TIME ds;// = malloc(sizeof(struct ET_LOCAL_TIME));
			getLocalTime(&ds);
			
			//Insert time into buffer
			printf("Writing time: %d\n",ds.time);
			buff[104] = ds.time & 0xff;
			buff[105] = (ds.time >> 8) & 0xff;
			buff[106] = (ds.time >> 16) & 0xff;
			buff[107] = (ds.time >> 24) & 0xff;
			
			//Insert valid value into buffer
			printf("Writing valid: %d\n",ds.valid);
			buff[108] = ds.valid;

			//Write buffer to client
			write(sockfd, buff, sizeof(buff));
		}
		else if (strcmp(command,GLOS) == 0){
			if (sizeof(buff) < 121){
				printf("Command %s buffer too short!\n",strcat(command,null));
				return NULL;
			}
			printf("Command: %s\n", strcat(command,null)); 
			GET_LOCAL_OS ds;// = malloc(sizeof(struct GET_LOCAL_OS));
			getLocalOS(&ds);
			
			printf("Writing OS: %s\n", ds.OS);
			//Copy OS name into buff
			int i;
			for (i = 104;i<120;i++){
				buff[i] = ds.OS[i - 104];
			}
			
			//Copy valid value into buff
			printf("Writing valid: %d\n",ds.valid);
			buff[120] = ds.valid;
			
			//Write to client
			write(sockfd, buff, sizeof(buff));
		}
	        //Exit on 'die' command
	        /*else if (strncmp("die", buff, 4) == 0) { 
	            	printf("Ordered to die... :(\n"); 
	            	break; 
	        } */

		else{
			printf("Invalid command: %s\n",command);
	    	write(sockfd, "Invalid command", 15);
		}
    }	
    bzero(buff, MAX);
  
} 
/*
 * Function CmdAgent: value:
 * -------------------------
 *  => This thread sets up a TCP server that accepts commands from a remote client.
 *
 *  => Code adapted from geeksforgeeks.com
 */

void cmdAgent(){
	printf("CmdAgent running...\n");
	
	int sockfd, connfd, len; 
	struct sockaddr_in servaddr, cli; 

	// socket create and verification 
	sockfd = socket(AF_INET, SOCK_STREAM, 0); 
	if (sockfd == -1) { 
    	printf("socket creation failed...\n"); 
    	exit(0); 
	} 
	//else
    	//printf("Socket successfully created..\n"); 
	bzero(&servaddr, sizeof(servaddr)); 


	// assign IP, PORT 
	servaddr.sin_family = AF_INET; 
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port = htons(PORT); 


	// Binding newly created socket to given IP and verification 
	if ((bind(sockfd, (SA*)&servaddr, sizeof(servaddr))) != 0) { 
    	printf("socket bind failed...\n"); 
    	exit(0); 
	} 
	//else
   		// printf("Socket successfully binded..\n"); 

	// Now server is ready to listen and verification 
	if ((listen(sockfd, 5)) != 0) { 
    	printf("Listen failed...\n"); 
    	exit(0); 
	} 
	//else
    	//printf("CMD Agent listening..\n"); 
	len = sizeof(cli); 

	// Accept the data packet from client and verification
	printf("Listening to commands on port %d...\n",PORT);
	pthread_t thread1;
	
	while (1){
		connfd = accept(sockfd, (SA*)&cli, &len); 
		if (connfd < 0) { 
    		printf("server acccept failed...\n"); 
    		break; 
		} 
		//else
    		//printf("server acccept the client...\n"); 

		// Function for chatting between client and server 
		pthread_create(&thread1,NULL, cmdHandler,&connfd);	
		//pthread_join(thread1, NULL); 
    		//listener(connfd); 
			
	} 
  
	close(sockfd); 

}

int main(int argc, char *argv[]){ 
	int port = PORT;
	if (argc >= 2){
		//Agent's TCP port
		PORT = atoi(argv[1]);
	}

	cmdAgent();
	printf("\nRPC AGENT closed..!\n");
	exit(0); 
}
