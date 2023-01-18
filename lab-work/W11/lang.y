%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token CHARACTER
%token STRING
%token INTEGER
%token VOID
%token FUNCTION
%token IF
%token ELSE
%token WHILE
%token FOR
%token IN
%token READ
%token RANGE
%token WRITE
%token ENDLN

%token colon
%token plus
%token minus
%token mul
%token division
%token mod
%token eq
%token less
%token more
%token equal
%token different
%token lessOrEqual
%token moreOrEqual
%token leftShift
%token rightShift

%token leftSquareBracket
%token rightSquareBracket
%token leftRoundBracket
%token rightRoundBracket
%token semicolon
%token space
%token tab

%token IDENTIFIER
%token INTCONST
%token STRCONST
%token CHARCONST

%token comma

%start program

%%

program : FUNCTION IDENTIFIER leftRoundBracket decllist rightRoundBracket colon functype cmpdstmt

decllist : declaration | declaration comma decllist | /*Empty*/

declaration : simpldeclaration | extnddeclaration
simpldeclaration : IDENTIFIER colon type
extnddeclaration : IDENTIFIER colon type assignment

functype : VOID | type
type : singltntype | arraytype
singltntype : CHARACTER | STRING | INTEGER
arraytype : singltntype leftSquareBracket nr rightSquareBracket | STRING

assignment : equal expression
stmt : simplstmt | structstmt
simplstmt : assignstmt | iostmt
assignstmt : IDENTIFIER assignment

structstmt : cmpdstmt | ifstmt | whilestmt | forstmt
cmpdstmt : stmt | stmt stmtlist | /*Empty*/

ifstmt : IF condition colon stmt elsestmt
elsestmt : ELSE colon stmt | /*Empty*/
whilestmt : WHILE condition colon stmt
forstmt : FOR innerforstmt IN forrangestmt colon stmt
innerforstmt : IDENTIFIER | leftRoundBracket simpldeclaration rightRoundBracket
forrangestmt : IDENTIFIER | ARRAYCONST

iostmt : instmt | outstmt
outstmt : WRITE leftshiftstmt
leftshiftstmt : leftShift IDENTIFIER | leftShift IDENTIFIER leftshiftstmt
outstmt : READ rightshiftstmt
rightshiftstmt : rightShift IDENTIFIER | rightShift IDENTIFIER rightshiftstmt

condition : extndexpression RELATION expression
extndexpression : expression | leftRoundBracket extnddeclaration rightRoundBracket
expression : expression OPERATOR term | term
factor : leftRoundBracket expression rightRoundBracket | IDENTIFIER

OPERATOR : plus | minus | division | mul | mod
RELATION : less | lessOrEqual | eq | different | moreOrEqual | more | equal
ARRAYCONST : leftSquareBracket { ARRAYELEMCONST } rightSquareBracket
INNERARRAYCONST : ARRAYELEMCONST comma INNERARRAYCONST | ARRAYELEMCONST
ARRAYELEMCONST : INTCONST | CHARCONST | STRCONST

%%

yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tProgram is syntactically correct.\n");
}