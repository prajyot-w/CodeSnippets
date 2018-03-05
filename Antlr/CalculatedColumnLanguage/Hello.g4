grammar Hello;

r        : FUNCTION;

VARIABLE : '${'[a-zA-Z0-9_]+'}';
FUNCTION : RANK ;
IF       : 'IF(' VARIABLE ',' VARIABLE ',' VARIABLE ')';
RANK     : 'RANK(' VARIABLE ',' VARIABLE ',' VARIABLE ')';
WS       : [ \t\r\n]+ -> skip;
