
public class SuperClass {
	
	static {
		System.out.println("SuperClass init");;
	}
	
	public static int value = 123;  			/*-------print:SuperClass init123  -----
													ԭ���Ƕ��ھ�̬�ֶΣ�ֻ��ֱ�Ӷ�������ֶε���Żᱻ��ʼ����
													���ͨ�������������ø����ж���ľ�̬�ֶΣ�
													ֻ�ᴥ������ĳ�ʼ�������ᴥ������ĳ�ʼ��*/
//	public static final int value = 123;  		/*-------print:123  -----
												/*ԭ���Ǳ�final���ι������������г�ʼ��*/
}
