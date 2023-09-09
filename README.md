# Compiler-Design

This is my coursework of designing a simple compiler that compiles a subset of C#

Part 1 (parser): The goal of part I is to write a lexical and syntactic analyzer - a parser - for a subset of C; mini-C.
Parsing consists of three parts:

  Scanner: the job of the scanner is to read the input file one character at a time.
  Lexer: the lexer transforms the stream of characters into a stream of tokens. These tokens represent the lexeme (i.e. a word in natural languages)
  Parser: the parser finally consumes the tokens and determine if the input conforms to the rule of the grammar.


Part 2 (AST builder + semantic analyzer): The goal of part II is to implement the rest of the front-end all the way to semantic analysis.
This will involve modifying the parser so that it can build the Abstract Syntax Tree (AST) corresponding to the input program and then perform semantic analysis.
In order to achieve this goal, I must perform four tasks.

  First, follow the abstract grammar specification and design the Java classes that represent the AST as seen during the course.
  Then, write an AST printer in order to output the AST into a file.
  Thirdly, modify the parser so that it builds the AST as I'm parsing the tokens.
  Finally name and type analysis.


Part 3 (code generator): The goal of part III is to write the code generator, targeting MIPS32 assembly.
For this part, I am only using virtual registers (except for special purpose registers such as $sp, $fp, ...).



The difficulties of this program:
  1. For Part 1, the designer must let the compiler accept all valid user's language which contains lots of corner cases.
  2. Using different visitor to do semantic analysis in Part 2. The designer must design lots of visitors that may only deal with several part of the input code. For example, 
  for the "struct declaration", I need to write an additional visitor which is quite different from the semantic checker of "variable declaration".
