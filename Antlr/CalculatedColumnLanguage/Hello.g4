grammar Hello;

VARIABLE : '${'[-a-zA-Z0-9_ ]+'}';
NUMBER   : [0-9.]+ ;
TEXT     : '\''[a-zA-Z0-9 ]+'\'';
WS : [ \t\r\n]+ -> skip ;

PLUS   : '+' ;
MINUS  : '-' ;
TIMES  : '*' ;
DIVIDE : '/' ;
POW    : '^' ;
LPAREN : '(' ;
RPAREN : ')' ;

EQ     : '=' ;
LT     : '<' ;
GT     : '>' ;
LTE    : '<=';
GTE    : '>=';

NULL   : 'null'| 'Null' | 'NULL' ;
ORDER  : 'ASC' | 'DESC' | 'desc' | NULL ;

variable : VARIABLE | NUMBER | TEXT;

equation
  : (variable | expression) relop (variable | expression) ;

expression
  : multiplyingExpression ((PLUS | MINUS) multiplyingExpression)* ;

multiplyingExpression
  : powExpression ((TIMES | DIVIDE) powExpression)* ;

powExpression
  : signedAtom (POW signedAtom)* ;

signedAtom
  : PLUS signedAtom
  | MINUS signedAtom
  | func
  | atom ;

atom
  : variable
  | LPAREN expression RPAREN ;

relop
  : EQ
  | LT
  | GT
  | LTE
  | GTE ;

if_fn
  : 'IF' LPAREN (equation | variable) ',' (equation | variable) ',' (equation | variable) RPAREN ;

rank_fn
  : 'RANK' LPAREN VARIABLE ',' (VARIABLE | NULL) ',' (ORDER | NULL) RPAREN ;

func
  : if_fn
  | rank_fn ;

root: equation | expression | func ;
