
public class SuperClass {
	
	static {
		System.out.println("SuperClass init");;
	}
	
	public static int value = 123;  			/*-------print:SuperClass init123  -----
													原因是对于静态字段，只有直接定义这个字段的类才会被初始化，
													因此通过其子类来引用父类中定义的静态字段，
													只会触发父类的初始化而不会触发子类的初始化*/
//	public static final int value = 123;  		/*-------print:123  -----
												/*原因是被final修饰过，不会对类进行初始化*/
}
