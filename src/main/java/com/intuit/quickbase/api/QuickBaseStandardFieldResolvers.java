package com.intuit.quickbase.api;

/**
 * The class {@link QuickBaseStandardFieldResolvers} predefines some instances of {@link QuickBaseFieldResolver}
 * for commonly used primitive java datatypes.
 * 
 *
 * @author Brad Brown
 * @version $Revision: 13 $ $Change: 714052 $
 */

public class QuickBaseStandardFieldResolvers {

	public static QuickBaseFieldResolver<String> STRING_RESOLVER = new QuickBaseFieldResolver<String>() {
		@Override
		public String resolve(String str) {
			return str;
		}
		
		@Override
		public String toString(String field) {
			return field;
		}
	};
	
	public static QuickBaseFieldResolver<Integer> INTEGER_RESOLVER = new QuickBaseFieldResolver<Integer>() {
		@Override
		public Integer resolve(String str) {
			return Integer.valueOf(str);
		}
		
		@Override
		public String toString(Integer field) {
			return field.toString();
		}
	};
	
	public static QuickBaseFieldResolver<Long> LONG_RESOLVER = new QuickBaseFieldResolver<Long>() {
		@Override
		public Long resolve(String str) {
			return Long.valueOf(str);
		}
		
		@Override
		public String toString(Long field) {
			return field.toString();
		}
	};
	
	public static QuickBaseFieldResolver<Boolean> BOOLEAN_RESOLVER = new QuickBaseFieldResolver<Boolean>() {
		@Override
		public Boolean resolve(String str) {
			return Boolean.valueOf(str);
		}
		
		@Override
		public String toString(Boolean field) {
			return field.toString();
		}
	};
	
}
