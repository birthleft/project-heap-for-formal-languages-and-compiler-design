/* 
	Tokens.
*/
#ifndef YYTOKENTYPE
#define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     CHARACTER = 258,
     STRING = 259,
     INTEGER = 260,
     VOID = 261,
     FUNCTION = 262,
     IF = 263,
     ELSE = 264,
     WHILE = 265,
     FOR = 266,
     IN = 267,
     READ = 268,
     RANGE = 269,
     WRITE = 270,
     ENDLN = 271,
     colon = 272,
     plus = 273,
     minus = 274,
     mul = 275,
     division = 276,
     mod = 277,
     eq = 278,
     less = 279,
     more = 280,
     equal = 281,
     different = 282,
     lessOrEqual = 283,
     moreOrEqual = 284,
     leftShift = 285,
     rightShift = 286,
     leftSquareBracket = 287,
     rightSquareBracket = 288,
     leftRoundBracket = 289,
	 rightRoundBracket = 290,
	 semicolon = 291,
	 space = 292,
	 tab = 293,
	 IDENTIFIER = 294,
	 INTCONST = 297,
	 STRCONST = 296,
	 CHARCONST = 297,
	 comma = 298
   };
#endif

/* Tokens.  */
#define CHARACTER 258
#define STRING 259
#define INTEGER 260
#define VOID 261
#define FUNCTION 262
#define IF 263
#define ELSE 264
#define WHILE 265
#define FOR 266
#define IN 267
#define READ 268
#define RANGE 269
#define WRITE 270
#define ENDLN 271
#define colon 272
#define plus 273
#define minus 274
#define mul 275
#define division 276
#define mod 277
#define eq 278
#define less 279
#define more 280
#define equal 281
#define different 282
#define lessOrEqual 283
#define moreOrEqual 284
#define leftShift 285
#define rightShift 286
#define leftSquareBracket 287
#define rightSquareBracket 288
#define leftRoundBracket 289
#define rightRoundBracket 290
#define semicolon 291
#define space 292
#define tab 293
#define IDENTIFIER 294
#define INTCONST 295
#define STRCONST 296
#define CHARCONST 297
#define comma 298


#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

extern YYSTYPE yylval;

