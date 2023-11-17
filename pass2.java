import java.io.*;
import java.util.*;

class pass2
{
public static void main(String args[])throws Exception
 {

String s;
String s1[]=new String[70];
String s2[]=new String[70];
String s3[]=new String[70];


FileWriter f1=new FileWriter("/home/student/Desktop/pass2/output3.txt");
BufferedWriter  b1=new BufferedWriter(f1);

FileReader f2=new FileReader("/home/student/Desktop/pass2/intermedi1.txt");
BufferedReader b2=new BufferedReader(f2);

FileReader f3=new FileReader("/home/student/Desktop/pass2/symbol2.txt");
BufferedReader b3=new BufferedReader(f3);

FileReader f4=new FileReader("/home/student/Desktop/pass2/literal2.txt");
BufferedReader b4=new BufferedReader(f4);

int m=0;
while((s=b2.readLine())!=null)
{
	
	StringTokenizer st=new StringTokenizer(s);
	while(st.hasMoreTokens())
	{
	s1[m]=st.nextToken();
	m++;
       }
}
int m1=0;
while((s=b3.readLine())!=null)
{
	
	StringTokenizer st=new StringTokenizer(s);
	while(st.hasMoreTokens())
	{
	s2[m1]=st.nextToken();
	m1++;
       }
}
int m2=0;
while((s=b4.readLine())!=null)
 {
	
	StringTokenizer st=new StringTokenizer(s);
	while(st.hasMoreTokens())
	{
	s3[m2]=st.nextToken();
	m2++;
       }
 }

for(int i=0;i<m;i++)
{
	if("AD".equals(s1[i]) && "01".equals(s1[i+1]))
	{	
	b1.write(" ");
	}
	if("R1".equals(s1[i])||"R2".equals(s1[i])||"R3".equals(s1[i])||"R4".equals(s1[i]))
	{
	b1.write("\t"+s1[i+2]);
	b1.write(" ");


	}
       if("IS".equals(s1[i]))
	{
		b1.write(s1[i-1]+"\t");
 		b1.write(s1[i+1]+"\n");
	}
	
	if("L".equals(s1[i]))
	{
	
	for(int j=0;j<m2;j++)
	{
	    if(s1[i+1].equals(s3[j]))
	   {
           b1.write("\t");
     	   b1.write(s3[j+2]+"\n");
	   }

        }
	}

	if("S".equals(s1[i]))
	{
	
	for(int j=0;j<m1;j++)
	{
	    if(s1[i+1].equals(s2[j]))
	   {
             b1.write("\t");
     	     b1.write(s2[j+2]+"\n");
	   }

        }
	}

       if("DL".equals(s1[i])&&"01".equals(s1[i+1]))
         {
            b1.write(s1[i-1]+"\t");
             b1.write("-"+"\t");
             b1.write("-");
             b1.write("\n");
            b1.write("00"+"\t"+s1[i+3]+"\n");
 
         }
if(s1[i].equals("AD") && s1[i+1].equals("02"))
	{	
	b1.write(s1[i-1]+"\t");
        b1.write("-"+"\t");
        b1.write("-");
        b1.write("\n");
	b1.write("00"+"\t"+s1[i+3]+"\n");
	}
	 
}	

b1.close();
b2.close();
b3.close();
b4.close();
  }
}

