grammar Hello;

/* root     : if_fn | rank_fn;

VARIABLE : '${'[a-zA-Z0-9_ ]+'}';
WS       : [ \t\r\n]+ -> skip;

if_fn : 'IF(' VARIABLE  ',' VARIABLE ',' VARIABLE ')';
rank_fn : 'RANK(' (VARIABLE | if_fn) ',' VARIABLE ',' VARIABLE ')'; */



/* VARIABLE : [a-zA-Z_]+ ; */
VARIABLE : '${'[a-zA-Z0-9_ ]+'}';
NUMBER   : [0-9.]+ ;
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

ORDER  : 'ASC' | 'DESC' | 'null' | 'Null' | 'NULL' ;

variable : VARIABLE | NUMBER;

equation
  : (expression | variable) relop (expression | variable) ;

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
  : VARIABLE
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
  : 'RANK' LPAREN VARIABLE ',' VARIABLE ',' ORDER RPAREN ;

func
  : if_fn
  | rank_fn ;

root: equation | func ;
