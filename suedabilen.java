
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
//Sueda Bilen 150117044
//System Programming 1st project
public class suedabilen {

	public static void main(String[] args) throws FileNotFoundException  {
		Scanner byteType = new Scanner(System.in);
		System.out.print("Byte Ordering: ");
		String byteTypeEntered = byteType.nextLine();
		
		Scanner Size= new Scanner(System.in);
		System.out.print("Floating Point Size:  ");
		int size = Size.nextInt();
		if(size > 4) {
			System.out.println("SÝZE OF DATA TYPE MUST BE BETWEEN 1 TO 4!");
			System.exit(1);
		}
		
		Scanner path = new Scanner(System.in);
		System.out.print("Pls,enter the path of file:");
		String pathName = path.nextLine();
		File file = new File(pathName);
		Scanner input = new Scanner(file);
		String dataType;
		
		PrintWriter output = new PrintWriter("output.txt");
		
		while(input.hasNext()) {
			String str = input.next();
			System.out.println(str);
			if(str.contains("u")) {
				//System.out.println("unsigned");
				dataType = "Unsigned"; 
				String unsignedBinary,unsignedHex;
				String unsigned = str.replaceAll("[^0-9]","");
             	int pUnsigned = Integer.parseInt(unsigned);
				//System.out.println(pUnsigned);
				unsignedBinary = unsignedToBinary(pUnsigned);
				unsignedHex = binaryToHex(unsignedBinary);
				//System.out.println(""+unsignedBinary);
				if(byteTypeEntered.equalsIgnoreCase("Big Endian")) {
					System.out.println(unsignedHex);
					output.println(unsignedHex);
				}
				else if(byteTypeEntered.equalsIgnoreCase("Little Endian")){
					String littleEndian = hexToLittleEndian(unsignedHex,2);
					System.out.println(littleEndian);
					output.println(littleEndian);
				}
				
			}
			else if (str.contains(".") ) {
				//System.out.println("floating point");
				dataType = "Floating Point";
				String floatBinary = floatToBinary(new BigDecimal(str),size);
				output.println(floatBinary);
			}
			else {
				//System.out.println("signed");
				//size = 2;
				String signedBinary;
				String signedHex;
             	int pSigned = Integer.parseInt(str);
				dataType = "Signed";
				signedBinary = signedToBinary(2,pSigned);
				signedHex = binaryToHex(signedBinary);
				if(byteTypeEntered.equalsIgnoreCase("Big Endian")) {
					System.out.println(signedHex);
					output.println(signedHex);
				}
				else if(byteTypeEntered.equalsIgnoreCase("Little Endian")){
					String littleEndian = hexToLittleEndian(signedHex,2);
					System.out.println(littleEndian);
					output.println(littleEndian);
				}
				
			}
			
		}
		
		output.close();
	}
	
//this function will return floating point to the binary form
public static String floatToBinary(BigDecimal floatingPoint,int size) {
	int k = 0;
	switch (size) {
	case 1:
		k = 3;break;
	case 2:
		k = 9;break;
	case 3:
		k = 15;break;
	case 4:
		k = 21;break;
		default:
	}
	String binary = "";
	String signBit;
	int sign = floatingPoint.intValue();
	if(sign >= 0) {
		signBit = "0";
	}
	else if(sign < 0){
	      signBit = "1";
	}
	//returns floating point to binary format
	BigDecimal integer = floatingPoint.setScale(0,RoundingMode.FLOOR);
	BigDecimal fractional = floatingPoint.subtract(integer);
	
	StringBuilder mantissa = new StringBuilder();
	BigDecimal two = BigDecimal.valueOf(2);
	BigDecimal zero = BigDecimal.ZERO;
	
	while(integer.compareTo(zero) > 0) {
		BigDecimal[] result = integer.divideAndRemainder(two);
		mantissa.append(result[1]);
		integer = result[0];
	}
	mantissa.reverse();
	
	int numerator = 0;
    if(fractional.compareTo(zero) != 0) {
    	mantissa.append(".");
    }
	
    while(fractional.compareTo(zero) != 0) {
    	numerator++;
    	fractional = fractional.multiply(two);
    	mantissa.append(fractional.setScale(0,RoundingMode.FLOOR));
    	
    	if(fractional.compareTo(BigDecimal.ONE) >= 0) {
    		fractional = fractional.subtract(BigDecimal.ONE);
    		
    	}
    	if(numerator >= k) {
    		break;
    	}
    }
    binary = mantissa.toString();
    System.out.println(binary);
    int stop = mantissa.indexOf(".");
    int E = stop - 1;
    int bias = (int) (Math.pow(2, k-1) -1) ;
    int exponent = bias + E;
	String expBinary = "";
	int i=0;
	while(i < ((size*8)-k-1)) {
		if(exponent % 2 == 0)
			binary += "0";
		else binary += "1";
		exponent = exponent / 2 ;
		i++;
	}
	expBinary = reverseBinary(expBinary);
    binary = binary.replaceAll("[^0-9]","");
    binary = binary.substring(1,binary.length());
    //I couldn't do this part.
    if(mantissa.length() > k) {
    	
    }
    
	
	
    return binary;
	
}
	
	
//this function returns unsigned to the binary form
public static String unsignedToBinary(int pUnsigned) {
	int size = 2;
	String binary = "";
	int i=0;
	while(i < size*8) {
		if(pUnsigned % 2 == 0)
			binary += "0";
		else binary += "1";
		pUnsigned = pUnsigned / 2 ;
		i++;
	}
	binary = reverseBinary(binary);
	return binary;
}

//this function takes a reverse of binary form
public static String reverseBinary(String binary) {

	String reversed = "";
	for(int i = binary.length() - 1;i>=0;i--) {
		reversed = reversed + binary.charAt(i);
	}
	return reversed;
}

//this function returns signed integer to binary form
public static String signedToBinary(int size,int pSigned) {

	String binary ="";
	String oneBinary ="0000000000000001";
	int i=0;
	    //if the signed integer is positive
	    if(pSigned >= 0) {
	    	while(i<size * 8) {
	    	if(pSigned % 2 == 0)
	    		binary += "0";
	    	else binary += "1";
	    	pSigned = pSigned / 2;
	    	i++;
	    }	   
	    }
	    //if the signed integer is negative.
	    else {
	    	pSigned = pSigned * -1;
	    	while(i <size * 8) {
	    	if(pSigned % 2 == 0)
	    		binary += "1";
	    	else binary += "0";
	    	pSigned = pSigned / 2;
	    	i++;
	    	}
	    	binary = reverseBinary(binary);
	    	binary = addBinary(binary,oneBinary);
	    }
	return binary;
}

//method to add two binary String
static String addBinary(String x,String y) {
	String result = "";
	int sum = 0;
	int i = x.length() -1;
	int j = y.length() -1;
	while(i>=0 || j >= 0 || sum == 1) {
		sum += ((i >= 0) ? x.charAt(i) - '0': 0);
		sum += ((j >= 0) ? y.charAt(i) - '0': 0);
		
		result = (char)(sum % 2 + '0') + result;
		
		sum /= 2;
		
		i--;
		j--;
	}
	
	return result;
}

//this is the part for binary to hex
public static String binaryToHex(String binary) {
	String hex = "";
	//String hexadec = "";
	binary.trim();
	while(!(binary.length() % 4 == 0)) 
		binary = "0"+ binary;
	
	
    StringBuilder hexa = new StringBuilder();
	for(int iter = 0; iter < binary.length(); iter +=4) {
			hex = binary.substring(iter, iter + 4);
		
		switch(hex) {
		case "0000" :
			hexa.append("0");
			break;
		case "0001" :
			hexa.append("1");
			break;	
		case "0010" :
			hexa.append("2");
			break;
		case "0011" :
			hexa.append("3");
			break;	
		case "0100" :
			hexa.append("4");
			break;
		case "0101" :
			hexa.append("5");
			break;
		case "0110" :
			hexa.append("6");
			break;
		case "0111" :
			hexa.append("7");
			break;
		case "1000" :
			hexa.append("8");
			break;
		case "1001" :
			hexa.append("9");
			break;
		case "1010" :
			hexa.append("A");
			break;
		case "1011" :
			hexa.append("B");
			break;	
		case "1100" :
			hexa.append("C");
			break;	
		case "1101" : 
			hexa.append("D");
			break;
        case "1110" :
        	hexa.append("E");
        	break;
        case "1111" :
        	hexa.append("F");
        	break;
		}
		hex = hex + hexa;
		}
	hex = hex.substring(4,hex.length());
	return hex;
}

//this method returns hex value to Little Endian
public static String hexToLittleEndian(String hex,int size) {
   int bytes = hex.length() / 2;
   char[] littleEndian = new char[bytes * 2];
   
   for(int i = 0;i < bytes ;i++) {
	   
	   int reversedIndex = bytes - 1 - i;
	   littleEndian[reversedIndex *2] = hex.charAt(i*2);
	   littleEndian[reversedIndex*2+1] = hex.charAt(i * 2 + 1);
	   
   }
	/*String littleEndian ="";
    hex = hex.trim();*/
   /* int i = hex.length();
    String[] littleEndianArray= new String[i];
    
    for(int iter = 0;iter <=  hex.length()-1; iter += 2) {
    	littleEndianArray[i] = hex.substring(iter,iter+2);
    	i--;
    	
    }
    for(int j=0;j<i;j++) {
    	littleEndian += littleEndianArray[j];
    }*/
	return new String(littleEndian);	
}
}

	
