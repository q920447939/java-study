## C++类的创建与使用

![image-20221029142306704](F:\liming\md\od_img\image-20221029142306704.png)

1. 在已有的项目上右键 - 添加 - 类 （有时候不能选了添加类之后发现不能添加，那么就选择 新建项）

2. 依次添加 `.h`文件（头文件）和 `.cpp`文件

3. 头文件内容如下：

```c++

#pragma once
#include <iostream>
#include <string>

class Student
{
public:
	void InitializeData(const std::string& name, int score);
	void SetGrade(int val) { grade = val; };
	void ShowGrade();

private:
	int grade;
	std::string name;
};
```

4. cpp内容如下：
```c++
#include "stdafx.h"
#include "User.h"
void Student::InitializeData(const std::string& name_val, int grade_val)
{
	name = name_val;
	grade = grade_val;
}

void Student::ShowGrade()
{
	using namespace std;
	cout << name << " has grade of " << grade << "." << endl;
}
```

5. 开始调用：
```c++
#include "stdafx.h"
#include <stdlib.h>
#include "User.h"

int _tmain(int argc, _TCHAR* argv[])
{
	//char* fileBuffer =readfile("c:/fg2.exe");
	//char* tmpStream = fileBuffer;
	//打印dos头信息
	//printPeInfoMany(tmpStream);
	
	Student s1;
	Student s2;
	s1.InitializeData("zhangsan", 80);
	s2.InitializeData("lisi", 59);
	s1.ShowGrade();
	s2.ShowGrade();

	return 0;
}
```



另外 VS 右键不能截屏可以使用 搜狗输入法的截屏工具进行截屏