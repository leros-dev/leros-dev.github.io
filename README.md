# Leros, a Tiny Processor Core

An FPGA optimized tiny processor core for utility functions
(e.g., SW UART). The challenge is to get the resources below
500 LC and use just 2 RAM blocks. The processor is named after
the Greek island [Leros](https://en.wikipedia.org/wiki/Leros)
where the architecture was designed.

The Leros project is hosted at GitHub in [https://github.com/leros-dev](https://github.com/leros-dev).

Leros is documented in following two publications:

1. Martin Schoeberl.
[Leros: A Tiny Microcontroller for FPGAs](http://www.jopdesign.com/doc/leros.pdf).
In Proceedings of the 21st International Conference on Field Programmable Logic and Applications (FPL 2011), Chania, Crete, Greece, September 2011.
2. James Caska and Martin Schoeberl.
[Java dust: How small can embedded Java be?](http://www.jopdesign.com/doc/lerosjvm.pdf)
In Proceedings of the 9th International Workshop on Java Technologies for Real-Time and Embedded Systems (JTRES 2011), York, UK, ACM, September 2011.

A work-in-progress handbook is available as LaTeX sourcec at [Leros Handbook](https://github.com/leros-dev/leros-doc/tree/master/handbook)

## Architecture

Leros is an accumulator machine with a register file. Memory is accessed
via indirect load and store instructions.

## Leros Aims

*An accumulator instruction that does less than a typical RISC
instruction is probably more RISC than the typical load/store
register based RISC architecture.*

 * A simple architecture
   * Results in a cheap FPGA implementation
   * Easy to use in teaching
   * Just few instructions
 * Different bit width
   * 16-bit version for tiny microcontroller
   * 32-bit version as a reasonable target for C
   * 64-bit version for a Linux port

Further aims:

 * Serve as an example of a small Chisel project for the Chisel book
 * Use in teaching in fall 2018
 * Provide virtual memory (with paging) for a Linux port
 * Use for student projects
 * Use for manycore experiments (NoC with more than 9 cores on a DE2-115)


## Instruction Set Architecture

The instructions of Leros can be categorized into following types:

 * ALU operation with the accumulator and an immediate
 * ALU operation with the accumulator and a register
 * Load and store
 * Indirect load and store
 * Conditional branches
 * Jump and link
 * Shift right
 * Input and output

### Encoding

Instructions are 16 bits wide. The higher byte is used to encode the
instruction, the lower byte contains either an immediate value, a
register number, or a branch offset (part of the branch offset uses
also bits in the upper byte).

```
+--------+--------+
|iiiiiiii|nnnnnnnn|
+--------+--------+
```

For example `00001001.00000010` is an add immediate instruction that
adds 2 to the accumulator, where `00001000.00000011` adds the content
of R3 to the accumulator. For branches we use 3 of the instruction bits
for larger offsets.

### List of Instructions

Following table shows all currently defined instructions (21, if you include
all conditional branch variations).

Not all instruction bits are currently used (unused are marked with `-`).
Bit 0 selects between immediate and using a register. The following list
is the complete instruction set.


```
+--------+----------+
|00000---| nop      |
|000010-0| add      |
|000010-1| addi     |
|000011-0| sub      |
|000011-1| subi     |
|00010---| shr      |
|00011---| -        |
|00100000| load     |
|00100000| load i   |
|00100010| and      |
|00100011| andi     |
|00100100| or       |
|00100101| ori      |
|00100110| xor      |
|00100111| xori     |
|00101001| loadhi   |
|00101010| loadh2i  |
|00101011| loadh3i  |
|00110---| store    |
|001110-?| out      |
|000001-?| in       |
|01000---| jal      |
|01001---| -        |
|01010---| ldaddr   |
|01100--0| loadind  |
|01100--1| loadindbu|
|01110--0| storeind |
|01110--1| storeindb|
|1000nnnn| br       |
|1001nnnn| brz      |
|1010nnnn| brnz     |
|1011nnnn| brp      |
|1100nnnn| brn      |
|11111111| scall    |
+--------+----------+
```

#### Comments

`loadh` makes only sense for immediate values.

Can easily be extended to 64 bits when ignoring the immediate bit.
Load function from ALU could be dropped.

Load address and following load/store should be emitted as pair as they are
dependent. Possible interrupts should be disabled between those two instructions.

Why do we have a nop? addi 0 can serve as nop if needed.

## Getting Started

To run a small test program in the simulator execute:
```bash
make APP=test tools jsim
```

More targets (e.g., synthesize for an FPGA) can be found in the Makefile.

### LLVM Toolchain
Initially, pull and build the [leros-llvm](https://github.com/leros-dev/leros-llvm).

To compile a C program for the Leros architecture, execute:
```bash
clang -target leros32 -c foo.c
```
This will create an unlinked ELF object file which contains Leros machine code.
For emitting Leros assembly code, execute:
```bash
clang -target leros32 -S foo.c
```
If only the raw Leros instructions are needed, the built LLVM toolchain provides a tool for extracting specific segments from ELF object files:
```bash
llvm-objcopy foo.o --dump-section .text=foo.bin
```
This will dump the .text segment of the object file to a flat binary file, suitable for running on simulators.


*TODO: more descriptions*

## Leros Versions and Compilers

### Current Version

The initial version of Leros was designed as a 16-bit accumulator
machine and written in VHDL. Besides writing assembly programs
a Java JVM for microcontroller has been ported to support Leros.
Also a software simulator written in Java is available.

### Future Version

To provide a reasonable target for C programs we will extend Leros
to 32 bits and rewrite the hardware description in Chisel.
We will try to make Leros to be configurable being 16 or 32 bits.
LLVM will be adapted for Leros32 and feedback from this compiler
backend may result in changes in the instruction set.
This may break the compatibility with the VHDL version of Leros
and the Java compiler.

We aim to provide enough documentation and simulators so that this
version can be used in teaching of basic computer architecture.
