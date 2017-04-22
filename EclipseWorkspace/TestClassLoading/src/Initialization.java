
public class Initialization {

	static {
		System.out.println("Initialization class init"); /*此语句必输出，原因：虚拟机启动时，执行主类会首先初始化*/
	}
	public static void main(String[] args) {
//		System.out.println(SubClass.value);
		
		SuperClass[] sca = new SuperClass[10];   /*此时，无关于SuperClass的输出，即不会导致它初始化，原因是：
		 											创建数组，创建动作由字节码指令newArray触发，会触发另外一个相关类的初始化*/

	}

}
