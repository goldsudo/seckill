package com.goldsudo.exception;

/**
 * 重复秒杀异常（运行期异常）
 * 注意：只有运行期异常，才会触发实物，而编译期异常不会。
 *
 */
public class RepeatKillException extends  SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message,cause);
    }
}
