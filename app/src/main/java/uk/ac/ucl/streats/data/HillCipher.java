package uk.ac.ucl.streats.data;

import java.util.InvalidPropertiesFormatException;

public class HillCipher {

    public final int n = 3;

    public int getCharacterValue(char c) {
        if (48 <= c && c <= 57) {      // 0-9
            return c - 48;
        }
        else if (65 <= c && c <= 90) {      // A-Z
            return c - 65 + 10;
        }
        else if (97 <= c && c <= 122) {     // a-z
            return c - 97 + 10 + 26;
        }
        else if (c == '.') {
            return 62;
        }
        else if (c == '_') {
            return 63;
        }
        else {
            return -1;
        }
    }

    public final int[][] key = { // det = 24
            {
                    1, 3, 2
            },
            {
                    4, 6, 5
            },
            {
                    7, 1, 0
            }
    } ;

    public  final int[][] inv = new int[3][3];

    public int inverse(int b) {
        int inverse;
        int q, r, r1 = 65, r2 = b, t, t1 = 0, t2 = 1;

        while (r2 > 0) {
            q = r1 / r2;
            r = r1 - q * r2;
            r1 = r2;
            r2 = r;

            t = t1 - q * t2;
            t1 = t2;
            t2 = t;
        }

        if (r1 == 1) {
            inverse = t1;
            if (inverse < 0)
                inverse = inverse + 65;
            return inverse;
        }

        return -1;
    }

    public void inverseMatrix(int[][] key, int[][] inv) {
        int [][] cofactors = new int[3][3];
        int [][] A = new int [3][3];

        cofactors[0][0] = key[1][1] * key[2][2] - key[2][1] * key[1][2];
        cofactors[0][1] = -(key[1][0] * key[2][2] - key[2][0] * key[1][2]);
        cofactors[0][2] = key[1][0] * key[2][1] - key[1][1] * key[2][0];
        cofactors[1][0] = -(key[0][1] * key[2][2] - key[2][1] * key[0][2]);
        cofactors[1][1] = key[0][0] * key[2][2] - key[2][0] * key[0][2];
        cofactors[1][2] = -(key[0][0] * key[2][1] - key[2][0] * key[0][1]);
        cofactors[2][0] = key[0][1] * key[1][2] - key[0][2] * key[1][1];
        cofactors[2][1] = -(key[0][0] * key[1][2] - key[1][0] * key[0][2]);
        cofactors[2][2] = key[0][0] * key[1][1] - key[1][0] * key[0][1];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                A[i][j] = cofactors[j][i];
            }
        }

        int det = key[0][0] * cofactors[0][0] + key[0][1] * cofactors[0][1] + key[0][2] * cofactors[0][2];

        if (det % 2 == 0 || det % 13 == 0) {
            System.out.println("The key is invalid");
            System.exit(1);
        }

        int invs = inverse(det);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inv[i][j] = A[i][j] * invs;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (inv[i][j] >= 0) {
                    inv[i][j] = inv[i][j] % 65;

                } else {
                    for (int x = 0; ; x++) {
                        if (x * 65 + inv[i][j] > 0) {
                            inv[i][j] = x * 65 + inv[i][j];
                            break;
                        }
                    }

                }
            }

        }
    }

    public  String encrypt(String text) throws InvalidPropertiesFormatException {
        return getString(text, key);
    }

    public  String getString(String text, int[][] key) throws InvalidPropertiesFormatException {
        StringBuilder c = new StringBuilder();
        int k = 0;
        int[] input = new int[3];

        while (k < text.length()) {
            input[0] = getCharacterValue(text.charAt(k++));

            if (k < text.length()) {
                input[1] = getCharacterValue(text.charAt(k++));
            }
            else {
                input[1] = 64;
            }

            if (k < text.length()) {
                input[2] = getCharacterValue(text.charAt(k++));
            }
            else {
                input[2] = 64;
            }

            for (int i : input) {
                if (i == -1) {
                    throw new InvalidPropertiesFormatException("Invalid password");
                }
            }


            for (int i = 0; i < n; i++) {
                int cipher = 0;
                for (int j = 0; j < n; j++) {
                    cipher +=  key[i][j] * input[j];
                }
                c.append( (char) ((cipher % 65)));

            }
        }

        return c.toString();
    }

    public   String decrypt(String s, int[][] inv) throws InvalidPropertiesFormatException {
        return getString(s, inv);
    }
}


//References: https://en.wikipedia.org/wiki/Hill_cipher#Operation
//https://www.techiedelight.com/hill-cipher-implementation/