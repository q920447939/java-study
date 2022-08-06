# C语言中的字符串练习



## 获取字符串的长度：

```c
int getstrlen(char* str){

	int count = 0 ;
	while (*(str+count) != 0)
	{
		count++;
	}
	return count;

} 


int _tmain(int argc, _TCHAR* argv[])
{
	char*  str = "china";
	int len = getstrlen(str);
	printf("字符串的长度是:%d",len); // 5
	return 0;
}


```

