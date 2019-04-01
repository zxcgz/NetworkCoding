#include <jni.h>
#include <string>
#include <android/log.h>

#define  ff_add(a, b)    (a^b)
#define  ff_sub(a, b)    (a^b)

#define  ff_mul(a, b)    (table_mul[a][b])
#define  ff_div(a, b)    (table_div[a][b])
/**
 * 矩阵运算
 */

typedef unsigned int FFType;
//有限域
int fFieldSize;
//
//乘法表和除法表
FFType *table_alpha;
FFType *table_index;
FFType **table_mul;
FFType **table_div;
//有限域表
FFType prim_poly[13] =
        {
/*	0 */    0x00000000,
/*  1 */    0x00000001,
/*  2 */    0x00000007,
/*  3 */    0x0000000b,
/*  4 */    0x00000013,
/*  5 */    0x00000025,
/*  6 */    0x00000043,
/*  7 */    0x00000089,
/*  8 */    0x00000187,
/*  9 */    0x00000211,
/* 10 */    0x00000409,
/* 11 */    0x00000805,
/* 12 */    0x00001053,
        };

int getRank(JNIEnv *env,jbyteArray matrix,int nRow,int nCol) ;
//乘法
FFType multiplication(FFType a, FFType b);
//除法
FFType division(FFType a, FFType b);
//指数
FFType exponent(FFType a, FFType n);
extern "C"
JNIEXPORT void JNICALL
Java_com_zxc_jni_MatrixJNI_init(JNIEnv *env, jobject instance, jint m,jint prim) {
    jint i = 0, j = 0;
    if (m>12)
        return;
    fFieldSize = 1<<m ;
    if (prim == 0)
        prim = prim_poly[m] ;
    table_alpha = (FFType*)malloc(sizeof(FFType)) ;
    table_index = (FFType*)malloc(sizeof(FFType)) ;
    table_div = (FFType**)malloc(sizeof(FFType*)) ;
    table_mul = (FFType**)malloc(sizeof(FFType*)) ;
    for (i = 0; i < fFieldSize ; i++) {
        table_mul[i] = (FFType*)malloc(sizeof(FFType)) ;
        table_div[i] = (FFType*)malloc(sizeof(FFType)) ;
    }
    table_alpha[0] = 1 ;
    //TODO 无符号整型中的-1
    table_index[0] = -1 ;

    for (i = 0;  i< fFieldSize ; i++) {
        table_alpha[i] = table_alpha[i-1]<<1;
        if (table_alpha[i]>=fFieldSize){
            table_alpha[i] ^= prim ;
        }
        table_index[table_alpha[i]] = i;
    }
    table_index[1] = 0 ;

    for (i = 0; i < fFieldSize ; i++) {
        for (j = 0; j < fFieldSize; ++j) {
            table_mul[i][j] = multiplication(i,j) ;
            table_div[i][j] = division(i,j) ;
        }
    }
}//矩阵初始化

//乘法
FFType multiplication(FFType a, FFType b) {
    if (0 == a || 0 == b)
        return 0;

    return table_alpha[(table_index[a] + table_index[b]) % (fFieldSize - 1)];
}

//除法
FFType division(FFType a, FFType b) {
    if (0 == a || 0 == b)
        return 0;

    return table_alpha[(table_index[a] - table_index[b] + (fFieldSize - 1)) % (fFieldSize - 1)];
}

//指数
FFType exponent(FFType a, FFType n) {
    if (a == 0 && n == 0) {
        return 1;
    }
    if (a == 0 && n != 0) {
        return 0;
    }
    return table_alpha[table_index[a] * n % (fFieldSize - 1)];
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_zxc_jni_MatrixJNI_inverse(JNIEnv *env, jobject instance, jbyteArray arrayData_,
                                          jint nK) {
    jbyte *olddata = (jbyte *) env->GetByteArrayElements(arrayData_, 0);
    jsize oldsize = env->GetArrayLength(arrayData_);
    unsigned char *pData = (unsigned char *) olddata;
    //判断下秩 若是不满秩，则返回NULL
    jint rank = getRank(env, arrayData_, nK, nK);
    if (rank != nK) {
        env->ReleaseByteArrayElements(arrayData_, olddata, 0);
        return NULL;
    }
    int k = nK;
    int nCol = nK;
    //初始化有限域
    //gf_init(8, 0x00000187);
    //unsigned int M[k][k];
    unsigned int **M = new unsigned int *[k];
    for (int i = 0; i < k; ++i) {
        M[i] = new unsigned int[k];
    }
    // k = nCol = nRow;
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            M[i][j] = pData[i * k + j];  // Copy the coefficient to M.
        }
    }

    //unsigned int IM[k][k];
    unsigned int **IM = new unsigned int *[k];
    for (int i = 0; i < k; ++i) {
        IM[i] = new unsigned int[k];
    }
    // Init
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            if (i == j) {
                IM[i][j] = 1;
            }
            else {
                IM[i][j] = 0;
            }
        }
    }
    /************************************************************************/
    /* Step 1. Change to a lower triangle matrix.                           */
    /************************************************************************/
    for (int i = 0; i < nCol; i++) {
        for (int j = i + 1; j < nCol; j++) {
            // Now, the main element must be nonsingular.
            FFType temp = ff_div(M[j][i], M[i][i]);

            for (int z = 0; z < nCol; z++) {
                M[j][z] = ff_add(M[j][z], ff_mul(temp, M[i][z]));
                IM[j][z] = ff_add(IM[j][z], ff_mul(temp, IM[i][z]));
            }
        }
    }
    /************************************************************************/
    /* Step 2. Only the elements on the diagonal are non-zero.                  */
    /************************************************************************/
    for (int i = 1; i < nCol; i++) {
        for (int j = 0; j < i; j++) {
            FFType temp = ff_div(M[j][i], M[i][i]);
            for (int z = 0; z < nCol; z++) {
                M[j][z] = ff_add(M[j][z], ff_mul(temp, M[i][z]));
                IM[j][z] = ff_add(IM[j][z], ff_mul(temp, IM[i][z]));
            }
        }
    }
    /************************************************************************/
    /* Step 3. The elements on the diagonal are 1.                  */
    /************************************************************************/
    for (int i = 0; i < nCol; i++) {
        if (M[i][i] != 1) {
            FFType temp = M[i][i];
            for (int z = 0; z < nCol; z++) {
                M[i][z] = ff_div(M[i][z], temp);
                IM[i][z] = ff_div(IM[i][z], temp);
            }
        }
    }
/*
	LOGD("2Coeff, %d,  %d,  %d",IM[0][0],IM[0][1],IM[0][2]);
	LOGD("2Coeff, %d,  %d,  %d",IM[1][0],IM[1][1],IM[1][2]);
	LOGD("2Coeff, %d,  %d,  %d",IM[2][0],IM[2][1],IM[2][2]);
*/

    //unsigned char IMCopy[k * k];
    //这个误写成unsigned int就会导致求逆出错
    unsigned char *IMCopy = new unsigned char[k * k];
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            IMCopy[i * k + j] = IM[i][j];
        }
    }
    //清空有限域
    //gf_uninit();

    jbyteArray jarrRV = env->NewByteArray(k * k);
    jsize myLen = k * k;
    jbyte *jby = (jbyte *) IMCopy;
    env->SetByteArrayRegion(jarrRV, 0, myLen, jby);

    env->ReleaseByteArrayElements(arrayData_, olddata, 0);
    //释放数组
    for (int i = 0; i < k; ++i) {
        delete[] M[i];
        delete[] IM[i];
    }
    delete[] M;
    delete[] IM;
    delete[] IMCopy;
    return jarrRV;

}//矩阵求逆


extern "C"
JNIEXPORT jint JNICALL
Java_com_zxc_jni_MatrixJNI_getRank(JNIEnv *env, jobject instance, jbyteArray matrix_,
                                          jint nRow, jint nCol) {
    return getRank(env,matrix_,nRow,nCol) ;
}//矩阵求秩

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_zxc_jni_MatrixJNI_multiply(JNIEnv *env, jclass type, jbyteArray matrix1_, jint row1,
                                           jint col1, jbyteArray matrix2_, jint row2, jint col2) {
    jbyte *olddata1 = env->GetByteArrayElements(matrix1_, NULL);
    jbyte *olddata2 = env->GetByteArrayElements(matrix2_, NULL);

    unsigned char *pData1 = (unsigned char *) olddata1;

    //矩阵2
    jsize oldsize2 = env->GetArrayLength(matrix2_);
    unsigned char *pData2 = (unsigned char *) olddata2;

    // unsigned char pResult[row1 * col2];
    unsigned char *pResult = new unsigned char[row1 * col2];
    //gf_init(8, 0x00000187);
    //相乘
    unsigned char temp = 0;
    for (int i = 0; i < row1; ++i) {
        for (int j = 0; j < col2; ++j) {
            temp = 0;
            for (int k = 0; k < col1; ++k) {
                temp = ff_add(temp, ff_mul(pData1[i * col1 + k], pData2[k * col2 + j]));
            }
            pResult[i * col2 + j] = temp;
        }
    }
    //gf_uninit();
    //转化数组
    jsize myLen = row1 * col2;
    jbyteArray jarrResult = env->NewByteArray(myLen);
    jbyte *jbyte1 = (jbyte *) pResult;
    env->SetByteArrayRegion(jarrResult, 0, myLen, jbyte1);
    //释放空间
    delete[] pResult;

    env->ReleaseByteArrayElements(matrix1_, olddata1, 0);
    env->ReleaseByteArrayElements(matrix2_, olddata2, 0);
    return jarrResult;
}

int getRank(JNIEnv *env,jbyteArray matrix,int nRow,int nCol){
    jbyte *oldData = (jbyte *) matrix ;
    jsize oldsize = env->GetArrayLength(matrix);
    unsigned char *pData = (unsigned char *) oldData;
    //初始化有限域
    //gf_init(8, 0x00000187);

    //
    //  unsigned int M[nRow][nCol];  这种写法会造成多线程时出错
    unsigned int **M = new unsigned int *[nRow];
    for (int i = 0; i < nRow; ++i) {
        M[i] = new unsigned int[nCol];
    }

    unsigned int test = 0;
    for (int i = 0; i < nRow; i++) {
        for (int j = 0; j < nCol; j++) {
            test = pData[i * nCol + j];
            M[i][j] = pData[i * nCol + j];
        }
    }

    // Define a variable to record the position of the main element.
    int yPos = 0;

    for (int i = 0; i < nRow; i++) {
        // Find the main element which must be non-zero.
        bool bFind = false;
        for (int x = yPos; x < nCol; x++) {
            for (int k = i; k < nRow; k++) {
                if (M[k][x] != 0) {
                    // Exchange the two vectors.
                    for (int x = 0; x < nCol; x++) {
                        jboolean nVal = M[i][x];
                        M[i][x] = M[k][x];
                        M[k][x] = nVal;
                    }                                        // We have exchanged the two vectors.
                    bFind = true;
                    break;
                }
            }
            if (bFind == true) {
                yPos = x;
                break;
            }
        }

        for (int j = i + 1; j < nRow; j++) {
            // Now, the main element must be nonsingular.
            unsigned int temp = ff_div(M[j][yPos], M[i][yPos]);
            for (int z = 0; z < nCol; z++) {
                M[j][z] = (jboolean) (ff_add(M[j][z], ff_mul(temp, M[i][z])));
            }
        }
        //
        yPos++;

    }

    // The matrix becomes a scalar matrix. we need to make more elements become 0 with elementary transformations.
    yPos = 0;
    for (int i = 1; i < nRow; i++) {
        for (int j = 0; j < nCol; j++) {
            if (M[i][j] != 0) {
                // the main element is found.
                yPos = j;
                break;
            }
        }
        for (int k = 0; k < i; k++) {
            unsigned int temp = ff_div(M[k][yPos], M[i][yPos]);
            for (int z = 0; z < nCol; z++) {
                M[k][z] = (jboolean) (ff_add(M[k][z], ff_mul(temp, M[i][z])));
            }
        }
    }

    int nRank = 0;
    // Get the rank.
    for (int i = 0; i < nRow; i++) {
        int nNonzero = 0;
        for (int j = 0; j < nCol; j++) {
            if (M[i][j] != 0) {
                nNonzero++;
            }
        }
        // If there is only one nonzero element in the new matrix, it is concluded an original packet is leaked.
        if (nNonzero > 0) {
            // Leaked.
            nRank++;
        }
    }
    //清空内存
    //gf_uninit();
    //释放内存
    for (int i = 0; i < nRow; ++i) {
        delete[] M[i];
    }
    delete[] M;
    return nRank;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_zxc_jni_MatrixJNI_free(JNIEnv *env, jobject instance) {

    int i = 0;

    free(table_alpha);
    free(table_index);

    for (i = 0; i < fFieldSize; i++) {
        free(table_mul[i]);
        free(table_div[i]);
    }
    free(table_mul);
    free(table_div);

}