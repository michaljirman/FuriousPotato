# FuriousPotato
Horrorho's Furious Potato.

### What is it?
Java [Heimdal](https://github.com/heimdal/heimdal) [ASN1](https://en.wikipedia.org/wiki/Abstract_Syntax_Notation_One) template ripper. It's able to rip [asn1_template](https://github.com/heimdal/heimdal/blob/master/lib/asn1/asn1-template.h) structures from binary files and reconstruct ASN1 templates. It also outputs informs in the style of OpenSSL's [asn1parse](https://www.openssl.org/docs/manmaster/apps/asn1parse.html).

It's a tidied version of a private tool I created whilst reverse engineering binaries for [InflatableDonkey](https://github.com/horrorho/InflatableDonkey)'s [DER](https://en.wikipedia.org/wiki/Distinguished_Encoding_Rules#DER_encoding) [management](https://github.com/horrorho/InflatableDonkey/tree/master/src/main/java/com/github/horrorho/inflatabledonkey/data/der).

### Sample Output Snippets
Templates:
```
TEMPLATES (asn1_template):
LOCATION: < TT      , OFFSET  , PTR      > : DESCRIPTION
10068cb0: < 00000000, 0000001c, 00000001 > : A1_OP_HEADER ELEMENTS=1 
10068cbc: < 30600001, 00000000, 100688a8 > : A1_OP_TAG TAG=1 CONSTRUCTED APPLICATION 
100688a8: < 00000000, 0000001c, 00000001 > : A1_OP_HEADER ELEMENTS=1 
100688b4: < 30200010, 00000000, 10068b90 > : A1_OP_TAG TAG=16(SEQUENCE) CONSTRUCTED UNIVERSAL 
10068b90: < 00000000, 0000001c, 00000006 > : A1_OP_HEADER ELEMENTS=6 
10068b9c: < 10000000, 00000000, 10068c5c > : A1_OP_TYPE 
10068c5c: < 00000000, 00000004, 00000001 > : A1_OP_HEADER ELEMENTS=1 
10068c68: < 30000002, 00000000, 10068b24 > : A1_OP_TAG TAG=2(INTEGER) PRIMITIVE UNIVERSAL 
10068b24: < 00000000, 00000004, 00000001 > : A1_OP_HEADER ELEMENTS=1 
```

Detailed Inform:
```
LOCATION > TARGET   : INFORM (experimental):
10068cbc > 100688a8 :   appl[1]
100688b4 > 10068b90 :    SEQUENCE (6)
10068c68 > 10068b24 :     INTEGER
10068c68 > 10068b24 :     INTEGER
10068bb4 > 10068d7c :     OCTET_STRING
10068bc0 > 100689b0 :     cont[0] OPTIONAL
10068854 > 10068830 :      SEQUENCE
10068a40 > 10068a4c :       SEQUENCE (2)
10068c68 > 10068b24 :        INTEGER
10068a64 > 10068d7c :        OCTET_STRING
```

Inform:
```
INFORM (experimental):
  appl[1]
   SEQUENCE (6)
    INTEGER
    INTEGER
    OCTET_STRING
    cont[0] OPTIONAL
     SEQUENCE
      SEQUENCE (2)
       INTEGER
       OCTET_STRING
```

### Build
Requires [Java 8 JRE/ JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org).

[Download](https://github.com/horrorho/FuriousPotato/archive/master.zip), extract and navigate to the FuriousPotato-master folder:

```
~/FuriousPotato-master $ mvn package
```
The executable Jar is located at /target/FuriousPotato.jar

### Usage
```
~/FuriousPotato-master/target $ java -jar FuriousPotato.jar --help
Usage: FuriousPotato FILE DELTA LOCATION
Horrorho's Furious Potato. Heimdal ASN1 template ripper.

     --help     display this help and exit

FILE<file>    input file
DELTA<hex>    the delta in bytes that translates the executables asn1_template
              data segment location to it's corresponding file offset
LOCATION<hex> the data segment location of the top asn1_template

Example:
FuriousPotato PCS.dll -10001800 10068980

Example details:
Rips KeySet from Apple's iCloud PCS.dll (Version: 15.0.0.10.1 CRC32: E7533650)
KeySet asn1_template executable location 0x10068980
KeySet asn1_template file offset 0x00067180
Delta = 0x00067180 - 0x10068980 = -0x10001800
FILE=PCS.dll DELTA=-10001800 LOCATION=10068980

Project home:
https://github.com/horrorho/FuriousPotato
```

It's a basic reversing tool and it assumes that you know what you're doing. You'll need three arguments
- FILE, our file
- DELTA, the delta in bytes that translates the executables asn1_template data segment location to it's corresponding file offset
- LOCATION, the data segment location of the top asn1_template

As an example, we'll take Apple's [iCloud](http://www.apple.com/uk/icloud/setup/pc.html) PCS.dll (Windows Version: 15.0.0.10.1 CRC32: E7533650).

My disassembler loads the [DLL](https://en.wikipedia.org/wiki/Dynamic-link_library) with a 0x10001000 base. The top asn1_template location for KeySet is 0x10068980.

The hex dump is:
```
10068980 : 00000000 20000000 01000000 02006030 00000000 888a0610 00000000 14000000
100689a0 : 01000000 10002030 00000000 50890610 00000000 08000000 01000000 00000010
100689c0 : 00000000 48880610 00000000 20000000 01000000 10002030 00000000 b88a0610
100689e0 : 00000000 0c000000 02000000 00000010 00000000 5c8c0610 00000010 04000000
```

The corresponding hex data is found in the PCS.dll file at 0x00067180 using a hex editor.
```
00067180 : 00000000 20000000 01000000 02006030 00000000 888a0610 00000000 14000000
000671a0 : 01000000 10002030 00000000 50890610 00000000 08000000 01000000 00000010
000671c0 : 00000000 48880610 00000000 20000000 01000000 10002030 00000000 b88a0610
000671e0 : 00000000 0c000000 02000000 00000010 00000000 5c8c0610 00000010 04000000
```

The relative difference we require is 0x00067180 - 0x10068980 = -0x10001800.
Our tool is unopinionated/ clueless in regards to binary formats and needs to be supplied with this information.

Thus, 
- FILE = PCS.dll 
- DELTA = -10001800
- LOCATION = 10068980.

We can now run the tool. I've manually truncated the output sections as indicated.
```
~/FuriousPotato-master/target $ java -jar FuriousPotato.jar PCS.dll -10001800 10068980
FILE    : PCS.dll
DELTA   : 0xefffe800
LOCATION: 0x10068980

DUMP:
10068980 : 00000000 20000000 01000000 02006030 00000000 888a0610 00000000 14000000
100689a0 : 01000000 10002030 00000000 50890610 00000000 08000000 01000000 00000010
100689c0 : 00000000 48880610 00000000 20000000 01000000 10002030 00000000 b88a0610
100689e0 : 00000000 0c000000 02000000 00000010 00000000 5c8c0610 00000010 04000000

TEMPLATES (asn1_template):
ADDRESS : < TT      , OFFSET  , PTR      > : DESCRIPTION
10068980: < 00000000, 00000020, 00000001 > : A1_OP_HEADER ELEMENTS=1 
1006898c: < 30600002, 00000000, 10068a88 > : A1_OP_TAG TAG=2 CONSTRUCTED APPLICATION 
10068a88: < 00000000, 00000020, 00000001 > : A1_OP_HEADER ELEMENTS=1 
10068a94: < 30200010, 00000000, 10068cf8 > : A1_OP_TAG TAG=16(SEQUENCE) CONSTRUCTED UNIVERSAL 
10068cf8: < 00000002, 00000020, 00000006 > : A1_OP_HEADER ELEMENTS=6 ELLIPSIS
---TRUNCATED---

LOCATION > TARGET   : INFORM (experimental):
1006898c > 10068a88 :   appl[2]
10068a94 > 10068cf8 :    SEQUENCE (6)
10068d04 > 10068878 :     UTF8STRING
10068ca4 > 10068b0c :     SET
10068bf0 > 10068c74 :      SEQUENCE (2)
---TRUNCATED---

INFORM (experimental):
  appl[2]
   SEQUENCE (6)
    UTF8STRING
    SET
     SEQUENCE (2)
---TRUNCATED---
```

### Dump
The tool dumps a short segment of the location which can be used to verify it's ripping the intended data.
```
DUMP:
10068980 : 00000000 20000000 01000000 02006030 00000000 888a0610 00000000 14000000
100689a0 : 01000000 10002030 00000000 50890610 00000000 08000000 01000000 00000010
100689c0 : 00000000 48880610 00000000 20000000 01000000 10002030 00000000 b88a0610
100689e0 : 00000000 0c000000 02000000 00000010 00000000 5c8c0610 00000010 04000000
```

### How do I reverse binaries and find/ identify templates?
Sadly this is beyond the scope of our discussion.
