### 图:

![PE文件结构](PE文件结构.jpg)

### PDF:

![PE文件结构PDF](PE文件结构.pdf)

### 网站:

[PE文件结构HTML](https://www.mallocfree.com/interview/windows-1-pe.htm)



打印PE结构信息：

```cpp
// PrintPEInfo.cpp : 定义控制台应用程序的入口点。
//

#define _CRT_SECURE_NO_WARNINGS
#include "stdafx.h"
#include <stdlib.h>
#include<windows.h>

int filelength(FILE *fp);
char *readfile(char *path);
int length;


char* offset(char* fileBuffer,int offset){
	return fileBuffer+offset;
}


void printDos(char* fileBuffer){
	printf("=========================DOC头=========================\n\n\n");
	char* tmpFileBuffer = fileBuffer;
	printf("MZ标记：%c%c\n",*(tmpFileBuffer),*(tmpFileBuffer+1));
	tmpFileBuffer = offset(tmpFileBuffer,60);
	printf("PE文件签名的偏移地址(E_lfanew):0x%x",*((int*)(tmpFileBuffer)));
	printf("\n\n\n=========================DOC头=========================");
}


char *readfile(char *path)
{
	FILE *fp;

	char *ch;
	if((fp=fopen(path,"r"))==NULL)
	{
		printf("open file %s error.\n",path);
		exit(0);
	}
	length=filelength(fp);
	ch=(char *)malloc(length);
	fread(ch,length,1,fp);
	*(ch+length-1)='\0';

	printDos(ch);
	
	return ch;
}

int filelength(FILE *fp)
{
	int num;
	fseek(fp,0,SEEK_END);
	num=ftell(fp);
	fseek(fp,0,SEEK_SET);
	return num;
}


int _tmain(int argc, _TCHAR* argv[])
{
	FILE *fp;
	char *string;
	char* fileBuffer =readfile("c:/fg.exe");
	printDos(fileBuffer);

	return 0;
}
```







网上的课后答案

https://github.com/smallzhong/drip-education-homework