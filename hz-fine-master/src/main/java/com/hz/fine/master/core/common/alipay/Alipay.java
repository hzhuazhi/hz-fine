package com.hz.fine.master.core.common.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.hz.fine.master.core.model.alipay.AlipayH5Model;
import com.hz.fine.master.core.model.alipay.AlipayModel;
import com.hz.fine.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @Description 阿里支付的公共方法（支付宝）
 * @Author yoko
 * @Date 2019/12/19 16:56
 * @Version 1.0
 */
public class Alipay {

    // 500理财-2021001105669996
//    public static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVvR7XxYF71d13NrZfUaKegm0Eeti3x0RJumsdvKCaADLWqjmgzUPtt5QwHdJrp9GPvAKO5RiyoAS717lupn6dPKLb4HAAvBUDP4n7kWd/ccYopnCyNwelLhlRQuJKis0Kn0tMLXzH8A8gqhxHIranz/b9HI9QxAGj5q6u09pgFSNo0eYijgZSpmpcGE7sqKN3rsM3pBCPpdHeRF45TZOUkNm9tC88mZaLL1Ybg0uY4crGix0cX2DSlLkTRRUSxT6kxIVZwYrriy5cZE+BLtarwbXmUcbeA6utngU5kLb+Fs/0Rb9y+kNE1ufEsK3mj4OXGdJLvVc46XtzKGAnIrfpAgMBAAECggEAdeLqwV1RT7lUHmMIuYp/yONOCNTegfFzpcgKROMYXaYCYFasABkafbCmDiusWe1JYyvVp8Jd62Nn1qtD5gVWcVwnq4vLIgjxUhlX/KLBJOjh5WcEGoqQjk74x60skxsryk0W7uZw8sfgYQsMMET4IK3t+d/eROvLjsoyhJltt241oThs4CS4DUN2nDTOJRsGZsP3nkwAQ0Uqg7EKko4i27JvqwqVAR1RIc/2U0YYEefFLFqkqAq9yrYsRN7y9rC3/9pniQw+eDfFnmM4h0TD9O+XuvJ2Cvovh/CytnBDX56cljCUQfkXdXPlIMjPUXBL9Cp1Pf3sJKiSGpGhzYumAQKBgQDhKIUy0ZQmezwMubEK7yXLbIEExOCFtFgyKOtNjF02tfFCictjGDjgy9gtpvD//qfja8aNwo/dx7VpNXmu5HwtaQbb60Z/jmtOOWxzDunFN5x2+HwrJkmccS9wc015YGcBCm6fNoMNiT5FrTqLPADu5c8oIE2MPFNRYy1ScSvT8QKBgQCqP+mOibWU5CjLJiW4hPo+L0mdzzXx3RUShCF71CwDq5yJlKtzoNLbGlTLrUBAzlL0X40+JT19+XJ/qVgXgErwe9pc5/tk45fg0gzDzfRG5FH06KnXgg+LuPzJuq1pFJPIso/ieydwkKbPhFRqa4bBIGZ2co0EJtD+10zmD8M7eQKBgDYPG4PrC839cpp/jwFYD9P6ljOF7X5MoYxRhMVWuR6kqmtWWC3PqABZEOtNFLnx829H9iEdIJuYEndTLocONr5UZCAzKfcAQ8VBRkeNwhTm2Ds8R9u4Up4sEr7GAEjvG2wFjC/Pz8FVPU1PXSoG2xaAuK8iqlvMNBjrXftYTrSRAoGAYAy6yc3rgdrPIP2enmF3hQnMsJmsx42BS/b7pQ7o3hE9uWkE6bE8BJzwZhCShX1h07jsyLjd3mqTLG7v4iJhJRfa9Dt021CD6TTYOsfBHxXFHzT8TTd1adCt3WnSV8xiLAKBprbwY1NYqQ7xSTDsz5xl8kFiSYtUsdHp8onGDZECgYAJdU+TqyfyhKk+H92e6+BYQon89trmQtDmBbVlywC2F+T3SVXlgH0RF4cwenanKnkFRkh8DXymXV85lh956R1gaKdlVLK06zb9ZnWggGBSLr0cIWCChvyoj/Gd2s9SXEEcGGG7mQVNEZC71x2elrH8nrJ3VAxZPew6W7QdBUJQ9w==";
//    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAji9YdMSNYGWgCMKQrH8AuSZDryGKjws0U1zRjYAX6wh/ZQgFvg/NcBHtvRZy5mOPpbFa0EFHZrXXy6j6KY8pR+ht/38ncBAGOsvVcfX/aBkq2wyEUYtjW/pnK78k+iRW07Z1lyiPCesnTxsvQvMKadngoeQgau916hy7V8f+KZQmcbpFWiYR3gEoUzZvmXcfsHJOLwE7t2w4WqmP0/bUs24DFDIAL1nqTBbo9c4/0KLklwbrdJdBxS3lCeGzmI9SBj3tI/gw8qY+sk7op0z7JhK74Zt+l021Ig5T/AgJeuyWbYvXWl2f6XVznF1KbmG8VsAUc7MMXUEUVbg6LWKP2QIDAQAB";

    // 趣红人-2018031502376903
//    public static String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCQz9TKf+gTF6OZqxDpZ8wBp7k2ablZxdzJi5NGON9LK6H9nIkWpgnJLB8Lm2Gde3ZIZ9X6n34GKDGg2Dpe9b0q7BQt13sJ/yEE8H+W9H4Q/hjzvQs6UYuHAcK+d2tno4MM6KIT2X6zhbOj6kGkThuf7U0D+Eg29ydUyuWNfE0SkdkNxvAeQwe1S+1a/Fyy0EtvCYZpp+RrUFI5vgpqoCkAqDRUODJ4T4ceHbfnliE5TRoGPVI/CUc9Ni1rNptNk6ussdC4zcoTjHqJpcUwE2q62KiZm88FmBQo/R1pRfJFvjWqvWt6+gzTtIjetkY2cj+AqisKPKKxYvLsWuCBz+/tAgMBAAECggEATe05QjtwZI3NAQ2YTTIJCz75oTllf9TFCkQs3ZYPO0Fgq7xH3UM+ct5mWnWkIv3kWfRepr6bL68Dfd2+E4nD4UwnU7/oOynq1+CfmFk7WeOTA97QIvLs1Zrx9FMJHj1UcWbiiTH6R5sEX4nZBxTtMrOdRSyfl8yKgsuomemxA/Nc2LJxETSeotmaBh3YBV12Ix6VVka1rWNL5tjufa/9FQ5f4z4W51QHo5SYSxQp2PTximKczOgjtIzRNGn+bi7cC8ecomCUP8q2b7zMcwcuRBKt2Mutcg2k0lvigg1k1KBzjzfmbog5KV+2lwCwlTllOceMOnDAVWYCS3L2FYTW1QKBgQDe8f9S2Hkm/aWbqVCbKo0NODtUr5ZUCNVbDlYtQwHWzS42ckomrC18OV5UiE16rOKhn7ItiDjWjarKJX4/fNnw5i6Fi/ebHfHuB7cZ+qs3mfxSelv+dKh/gGNLITcyfm04vVQ4A3Gr4bwkS7DHobJSZPzvrP89rwGOK8W+1uIqzwKBgQCmSD0M+4g+bwtZfCFR/2KZDh0wjUm9tWgEhIRuuG//FDdiZkKNom5XWONzjCP6pjzDvQhHRvvfgKQQBoXLUXPysYXHy9O31oV1xtFR1La5d5kKJZjWE98W4Ez1MKX5kQ3QDEXCIWLwgBYNlAzZyjY09nn2T277WPZViMYwzI14gwKBgQDeCJrk8ixudYyaY1ygvBbwBIGqTJjlpkp+Pd/7gdFyELQmi1pn+2/tWOEmRP0M4ONwXbBBAnrAyyQ94GtEZV5UOZo5bHUzafZIvixP1kLwxA30QmIeICaznLTG3RSw2BKEwKIAiwWJTe5nI26y0snanzL8rAkjcIiXA+cTscRbJwKBgQCG+Qo9WIs+CosO5vhxA7k3/cHp4DXkPLUjPaH18dZPGkzenZ0WNKwWULvH1hB6a6fVRsLKgK8Au/3wWCsQX5ybekfaZvQDPKl2cZ0FLIHpyE+8cco+s0CZ5BXzzLpc+sZYgy24p7sU9xNvkCZiPoaDtTJIoi+27H5/7Zbak5+eGwKBgQCfvBLhUTC2PxJbCenxc0BBsTj27GI4dW/tzDXqS18yEJuozhuhGrbv1O8YRaKBp1AIgUwFbY4jjmmhswbfTqQZ204cQPK1YpdT3PrvvysJGiDe8T/a4xyaU+kwew8ZYMyyfwhra8c/bs2JdoCes3AYLZNGF6y6EfdeFyG4C8up+A==";
//    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlSOnfZNaMb/hbw7Gy1xZSDFw9nIWlgr90V0ttmEZ2EYsEyNljqcgItj3kAD30SlA2Ce9kkPO5Dhbm+MKXarVtTsCFq1g2WrngHmS6oF5xbIsI+nCPQygp36ATrj1MOjMkr4OvE6bdDLVkcV5GTMGSlIk5pXbjb0+W2IreWj9ZtotJvCToraJIw2pjmoDXkKq8IajevOBFiZSUY1WOIp2PWJ06Rhp5akr+2ItKWxgB65yLZoXT7pzd7CScVi+ONyh3VJp85Kp23qpGIz7gDQG9hBkiCV7le/dNcVTfVWdXtqH7Zs4X3d+36iDKU7C91yq11ptnAamGFeNjbaj+xpkVwIDAQAB";

    // 扎堆交友1-2019081366230241
    public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBdDRfDCAjMViNd0udfBJISjHIMS/JgXDM9XuUeqRZokvjB0R91glJQXQ7v07mQ8r4yKgiH7LWrUyrIxeYveznnj2W0JZNKFxkKjmYH3ymxM+i++gpQwOZrf9HieFw+uQehwxob06Momi03tGv3shds6yTq+yqiCFeLP2jmsjxeZzQed6PdNO03KB3AXLJc2Qt3vWMb3QeG4yoY0qrCj+9NtRJ+RcRkp+AXUHxoTJclupIeGQe/LLAb0oheP+YFyJqDWukt1iPG+GL2faHyo3wY6KctgEd+DHWA/i9VNU1Wrvw2B957OLfCFZmvzCyHpBEAfQOfG0WNCAOSGG5fN+LAgMBAAECggEAc3eLHxxZrgpdyhEWSSpZxFEphj8ZOMtUKfzsdcvOvISe26mn1fYL4mOXoZNKdOkTKkUQcx+DDSkWsa9jZMJDhXUHynXTGo9dmZpwNNgg/z+dVOsMcYFsvK8OfGjnxTaEMxaSYrSlg5t5eZslvZEhdzLERBOdzZ4O5UmME1oE0h03jrxpeyZf4hWDBN0IBqw9qUbX+xvEi7pu+kwZE1H75B46SDtSm/9fAEIg8vgkw7BDJJH72FJZ77g5jmX73ryo0RfzXUlAbaDSqCFMi4lCmfG02j28kKOsiBrZI6GPbP+oFrSscmh43TY11LYPGkpuLcNMmwMp+cRFU3QWdA1ugQKBgQDQttwbott73QOQOpPW35SkWh/OqUgag35ygm/tnRWxUwEzaApyi/WyfzKT+y1TsNuNet1vi/YpnYjaau7nvXj8XcQ3X5J7UUUVVXCI/KR2/DuLr0KQp6hLKKCB4Tc/fCcg5XYM3QVwE8RaXquAqIWG+kIvdV1B5/x0Ivd9NrSC4wKBgQCeyFpac7qu3ipkYST+zQSIcqUvf1KnmAaLmwYXpyZGN11Uemog1CcOJjGTfTEqHoTsY9j3HyxLHZC8RTQbjAg8euw8sMk5EqurIqt7tiVuoc5D7hryRghxOUkc06kLn0efkjYpARUVpXeK5B0zT80PoOjRiI0fEmhN0RGTgnNJOQKBgAXZqk/H/9j39/qZ+bzjJXp54DxduFyt5OR2zYZeidYfmoADYY+WZrurHxLQzpNy+KR9liljRfIirUxaBPpP8E06n55humxkechATdg5JK8FIPIoZnhBitEYgW9CjdbktwAQDoALzfQ3SaCy2KHRl3SSHTrRUlzfkM78j+1dlC+7AoGAIu3uOnG1Apzq9HQOYGMXeTDHN+CpfSbLYdCzkWZ62/138YqQaDJjeRBb3ZcvxpU6Jop8gzh/VPLnEROkN98hPLTJ3TwCuCRsUVRqJufV6jV8G6N4mA1h9A5nEAlb6aubZPYUJgcP7VgeKRavecc3VBXQwa9pZAl3mo7AcBPFarkCgYEAnaQjOYYRHBhQP1wNltncjlGF9ctbJnktqnx3XDQrKdCbqEGfoJsSQ3mYMrV30p4IOC0kSDGK0BHVV1PPyFizsRh6Bq1+N4UvcKV3A8PujvKG5mvsrkEThDRdxTtm4booCuB/0tSaWilAuMfJj/o02zKnv1iB/2grfRyz0Br7GyM=";
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgZDYZB2VimBJlSrFi81tw/wlcAyGXEzutihLufsm/0zTyTzFrmQCqD52cHK/OUZ4S9wgqPBywh3V+Cet97czSJTSYH/rACapzX1gyLT/DYZ/ui6GQvpTxtItLCjmwNCSKBkb/zf7jh6uhpz99q1p+wjBTcUNMgdBas4+2qepV5XOnyYMMqjWi+agL4k1m8eGX2LwB4xyQcwzroIrmqaTto93J06zY+3BEtOVn62a+1C1zSiwa1wxE+lflk00rb6nuo8Vj9zeAAFPNIF24Sufld1ihJCYyAUgy3KtOHTaxIcnHQhCK8EMDVjNru8RsJmTAmKIAv78A+bN8KSPYdvQ3wIDAQAB";

    // 跳橙
//    public static String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDbx1v3H8wJmmZ/08HLiYo1nByvgys3UnRu+PjmtvEM11o9E/t6Y40AQwhIhAXbbAAxYjrhoixDYJ9eZ8hC59eJsoIINwLwCO+vwen956XQdAaVPLgwR0mUXABXKcHuN17QFBJNXt+Cux7Oq/OzJJud0wVuaXt1oZ6Gx3/+iqITqHgfZBUgl1gnTbnxJ6P3zMqb/2SNG6arGkZGdkveGLzSa6S2qLIn42nI8n1GxrEOhLZJ5XWFbNWtE2ksRjir/I6LXGQS7v0RiXHqrEa8DWm0q/pXytWJZ6iKhBQ5VitLmLTn9PVgf7CRd0uX0ptWnWlI+V9T19uH/8A9yAgTvHBRAgMBAAECggEBALcU0dWBVXVCe9RMUIn/X3TdQUAsIpoJRBkVqjJ3f0JVO0TaGomP2Gu5a3MNF9v7kgWRcL95WyodmWUJS1gAzhpU6+FwAjB3IUPmY8gxan3FyZ4UYNNG71J5eYcir/yDf6YSslRljgfXXQiJUY+QFVpZ6ZJaqJALPIXGNyeR0QZRq90QHczyjzVInVvOgy954BfUJoaHCcqSSNZMa+bWPkB7GKxXC2BwJC1IysOrT+tn5pEX3W/b2/sirH2/MNWIsV7hOR99JgrYsQiYkQ4Iq26CnDMNePm4sDF0s9sh/4gnOolFcZUTc/GY5rU08M+V2dGsB1b6PlY12FNR1McEg2kCgYEA7mUNVxhaqiElkhMwyrjgu8PQ/NLlRj3KuQjrDF0Pwetp3mKQep0CNKpUBzkfa+AdXCY07Ol6Y0hNGX+9J40f2BHp/JtMFJTt2k0qxfTabMBNSnPOyCzbdVYW6Vlw1qsaLgq600aeu3UNz4uK7JHhY53mjpSY/vCyu77jVEkYJnsCgYEA7AJdSzCNgFybZyP+JlEe1ZAlL0RINnX52JVFsCjvONyNnAQi6Nn4J0LaLpts05QL+rEMBqoaLJYN/dFplyu9pcYL1V5XfWocPDetjf1GnpMzblGllX3aBY9RISjQHRtH8RcNzEJGk38bA8VAEUHUKivBD1LovmjSL+vUyW7I0KMCgYEA03kg1FnuU20D/wi3B53o/ac/BIewixbVdj7LAzSqfcNvLq8QqzQMeNt/nsi1buRoJw5ddKvIvbmtayk9ipBN50Y84rCAVOGn/Tbm8qO5/y63YYxJqpjgNL4hpO6KgmNV3fH2uOS0emXj0nBe1Gy0G8I+e8yly8GJS7KRxnrwyXECgYBq4tM/x1h+hvJ2rs1eqySM0kCU/Ja724hw94HdO1zEYtbbjuAElxsVJOjNbOTHmegm/GIW7pj2Emt5xYrNxSiZ2GzpkFWNXi41c33trYR7Mu17DA0y/BFurS6wFtzSIdXeMXO1S1rNWCZy+bV/W1HsW26PMxxnh++RdnwjUkIugwKBgQDSehj0piK4TgMibFs1DYMJIFse+rm8CPXgOt12bSW3MjjjQg7J4M2EHrqfUP7UQPfdeThyEYqlGGKd2CcFy0BeQi+OPAyVw0E6mQVcXzzLDWE1KBQdjv/NP4MMxV3rBN4Oy4z6j8Zk14O6sL6gsE9CrweDxTsAiaeU/Cjyzk3CIQ==";
    public static String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGL5+XWu1WUx+XX6zJHiYFrmvSb2IQ39E77nlBGoZx938+3GVbjFNHP+ZknfNE9pRrja+Up88nRjgiTwOh1recCHI+5S7UFVDHpl1xIgWVse3dgZG4f2EdwaIGFiOlH8hNjqMIl9yuKAi2OAggo3QrADpgsnOVWuib7EzcafGnmoEVdCdr1qD3wp2gj3hViPkyTJP+Il6u8cmt9SOX0xGKFO6HyTOMWVaJT1wt0GOhGRAKvEfqvbmpcGjXiOuV9yCh7fM6cPSi2fCXoNTxO12y3HGwYgD8WEvTdQU+XkbJeX7zILXWoZLQv2inTV2LTfmHBweTf27Ol98PveHoGlj7AgMBAAECggEAXDmRE2LAKouY894jEFtzQIZfvtMk8HoUAX/mwLMtYlj7kMXDuAcbGaY2gEL69xQOcFmCKsanHtQpMpNh6LJ9oXVAqC+2XreDvKSAJoh+lCMlXA94ivipU91Isaw3tX7BmWSGcxqE5wHG8w5Rr7UH26IdfWZJAIbpxnjDXt5F463QKbPTuSiEMrpfXpGW1Q1Zpdw+XpLXxLpFwFcm5pLrNtUq3JZ5wP+laTgUmGOcd0U3ZatDjKJ0uqebMDPPB+U9iTLrBN00xkF0ZgM4ETHsNC7xMye9fEkwR+gsjKeXtjS3jl/1C3k2pSdhflBWTMYvIiKb8dw9/Yrppu3ZrcvU6QKBgQDZmD5xxVnnZ/g7p5lZDHszAr7G0y4a0jd/t6ENCO615zm4Vjo1bi7BiqNwjuaXIyz9yYmx4eggOPLvaw3CXmw9puOYLfAYdcL42ofJrupYObZ2f9JpYpy/Dthsgw1AmMAR9A9csrsW58iI2cYVErvie4kDq9T+8QS53Ft9wPlGvwKBgQCd3qiO+APaTAjpW8Y1WEu5vlQkNgNRkUYcUhDSsYz/rGzRZKajuyn1zr7wNGMVqVExevGGusK74WslY6Sxw4NgWAZhtgolY0e6Y4XkozmmE2b7Ed1pge0sszHr0yhlkMoUZAaapTQF1BJiWRHWLQ63qFiFYgBKybHRXWwJzJsYxQKBgDMluz/pANoJMWtsWPp6mZcDBS8dQtAhK9SEqIG04M+3W3T+J86ZaLoryYFSxdcu+ItrTUeX74VrfYdTpbdTlKPlT8hcu/wz6jGuxI5e5Ez6u+4c26vT9z5d0fen0nylqzhNIzNJ+/pFX3jzn8EiVzHEkg9f28BVjSPXLVZg/vdnAoGAV3LybhfGkpLR0OpsdjpN4DslAtYEqpdNffybowqFL0+FC8y0XjgcOzRmIkrypBohhsYyqk4j9s0cfB6hKXNDAAhiqJedMS64me/tSER+d5eB0QwJWRuGlN9ChP53/yLgPcFWwy1GDwB1mpL6RWi6IVQYjPJHdSYa4P3eDeOP+n0CgYA9NLX7dhiBePTOOLQIgiDyGsiebdwyw3VCEYbuoRkcBBS077Qebr4SaLbB3R2JEzdXqRRVG+5NkuVISBcu7SwaAAJC8vG4XHfTV6FZHbGO4w4zVCxdIp5nhSc3z/MDWcp7Md8V/kABq+x8yFzsLCcqBxvR5kkJiDseu9REh2xV/Q==";


    // H5-500-2018031502376903
    public static String H5_APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHFNNqJxfqHeWD9gY3xOJURr5zzxAHxMYNwSQGCEBoXCq/zs51NOY7Um+66WT+JLf1hkztTDNRbLynkJYKoxRBBGgI3MiS4s1ShunxJp1e1wHnw0ytbzLrGM1hApWN7Hu75h04LBAuLoIdR6wX5MgxX3ez8oYvv7i94QmkSOGZWAih92Snm462J5+kt2+j/3MltKBBpRjMiYIZzAiMM1pRGmztmPJAuYsRT0RpKkSq24XvL8Ur6w6DABjMxhbpJncuNFfteZDZAjs7tLUfWylFJEPHmO0LkDUAPiLlehxb4O3dO7O+eOhU5pk33SKmGcix6ZS7XNJxpeNpMkw6ncEDAgMBAAECggEAS0En8wiqgwx61ESY/DLcoHT1H5Go7ZsLd5g1WIL6VumaC1fMn3GEHmJzyp7CW+/a6JcUflMod+FrTLL5VB28Z+hriRxvRZ3DI4n0BRwtKiwAbLzaF/lOVblwme4VoYaw/GJbvvKVZRUV+ff3ASy89vyntIGVjj5yTp5zf6n2vTWFqChdz6rN4Rovid8o4u3K0aMt1Z3jIlYzgKgkHoWN3Zr/qZR0+SqvhNeUWPpuDmUw2UBRG2UERu92g+R/zGLSmw3DaEsta0GWvjWLlyWwBvkAuCCco0UF6IMD1ypnSqBeo9bnH291VOVAgQoVw6aB5XIxi/vPni8YNtFj9QK/iQKBgQDF3IXYEkn2fNgFU7ZQLtJAb4iC61s+9THFe6x4vJOV5HOafs/ieg+VcQ3N2+AJF55Rm0F5tBw36Yd0FrElo5l85GY9CsjomYKGw5njzWvzi0Inb3sRVnwC8N48La8MdWRrx9hrCJRBDVdBymLIsrpiZ/Suf1gb2jQJharf/d3DNQKBgQCuxeDLkW/fs1x1IjJ43KCzozvqV943xE3NcIBbt6H68DmZxKCda3IQuNoG393yD1XcyPz0GUGARCRgAJKiYphoGPzj3X2Jqt/Mem46mBq6xC0RFSrt8f930lNFWh+sum9YdEzDWQjgfdi3B1CkDQwTulEREvEpJZtNAzssYxICVwKBgGuud30/ftdR3HMRuw/qW2zMBnxRwIgEA4FZrGTNJHkYKr4zM8oq6d5H995IQqZQH06Ec17b4dugEaLhUqmMXtU0rFueSfckzH+xZkgvHQgfivJRXqXx7m3fTuNrbXAXV3689ZBSy/SXYRyiG57kfkFeGDD7hyUpXNat03AyBoXdAoGAZlJhbq0iQ2S+D7HKvOnmh+VccbGp3xplto5UB951zfWQs47jveYm7NVciEPOrCYATfe56KtUpuS+KxqKvtlcgy2F+V42XkAVMKDJIjLlC01JWUP8UzyoJZ2UtPizrKul/rJwMrpQsSXcmCOHOdSNqosdZRKi8EUdLDRlE3KZG5cCgYEAghpahd0mvRkl011gZ2yn9ZjOmHj8Nk5A1ctP27WEYO5x4BfASvCYyN3Rckr/AmcxypFBCmotrEV36MqRdxTigp4eWa38EDjtkCFJDEBtDrQLoZa0xNQXIdNcUPldp+qztjKHXyj1IgSN9DZFTOwlWPKwumBX44ozd/76B2QwYVo=";
    public static String H5_ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAji9YdMSNYGWgCMKQrH8AuSZDryGKjws0U1zRjYAX6wh/ZQgFvg/NcBHtvRZy5mOPpbFa0EFHZrXXy6j6KY8pR+ht/38ncBAGOsvVcfX/aBkq2wyEUYtjW/pnK78k+iRW07Z1lyiPCesnTxsvQvMKadngoeQgau916hy7V8f+KZQmcbpFWiYR3gEoUzZvmXcfsHJOLwE7t2w4WqmP0/bUs24DFDIAL1nqTBbo9c4/0KLklwbrdJdBxS3lCeGzmI9SBj3tI/gw8qY+sk7op0z7JhK74Zt+l021Ig5T/AgJeuyWbYvXWl2f6XVznF1KbmG8VsAUc7MMXUEUVbg6LWKP2QIDAQAB";

    public static String ALIPAY_SEND_APP_ID = "2019081366230241";
    public static String ALIPAY_SEND_H5_APP_ID = "2018111362103999";

    /**
     * @Description: 组装阿里支付的订单号
     * @param alipayModel - 基本数据
     * @param notifyUrl - 同步地址
     * @return String
     * @author yoko
     * @date 2019/12/19 19:38
    */
    public static String createAlipaySend(AlipayModel alipayModel, String notifyUrl){
        String resultData;
        //实例化客户端
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                ALIPAY_SEND_APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(alipayModel.body);
        model.setSubject(alipayModel.subject);
        model.setOutTradeNo(alipayModel.outTradeNo);
        model.setTimeoutExpress(alipayModel.timeoutExpress);
        model.setTotalAmount(alipayModel.totalAmount);
        model.setProductCode(alipayModel.productCode);
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
//        model.setBody("我是测试数据");
//        model.setSubject("App支付测试Java");
////        model.setOutTradeNo(outtradeno);
//        model.setOutTradeNo("df_test_hzhz_001");
//        model.setTimeoutExpress("30m");
//        model.setTotalAmount("0.01");
//        model.setProductCode("QUICK_MSECURITY_PAY");
//        request.setBizModel(model);
//        request.setNotifyUrl("商户外网可以访问的异步地址");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            resultData = response.getBody();
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        return resultData;
    }
    
    
    /**
     * @Description: 支付宝单笔转账到账户
     * @param data - 要转账（提现）的数据
     * @return 
     * @author yoko
     * @date 2020/1/10 21:01 
    */
    public static AlipayFundTransUniTransferResponse transferAlipay(String data){
        try{
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
//            certAlipayRequest.setAppId("2017072507891356");
            certAlipayRequest.setAppId("2019072665936796");
            certAlipayRequest.setPrivateKey(privateKey);
            certAlipayRequest.setFormat("json");
            certAlipayRequest.setCharset("UTF-8");
            certAlipayRequest.setSignType("RSA2");
            certAlipayRequest.setCertPath(ComponentUtil.loadConstant.certPath);
            certAlipayRequest.setAlipayPublicCertPath(ComponentUtil.loadConstant.alipayPublicCertPath);
            certAlipayRequest.setRootCertPath(ComponentUtil.loadConstant.rootCertPath);
            DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

            AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
            request.setBizContent(data);
//            request.setBizContent("{" +
//                    "\"out_biz_no\":\"df-alipay-test-1\"," +
//                    "\"trans_amount\":1.00," +
//                    "\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
//                    "\"biz_scene\":\"DIRECT_TRANSFER\"," +
//                    "\"order_title\":\"段峰-测试-提现-1\"," +
//                    "\"payee_info\":{" +
//                    "\"identity\":\"duanfeng_1712@qq.com\"," +
//                    "\"identity_type\":\"ALIPAY_LOGON_ID\"," +
//                    "\"name\":\"段峰\"," +
//                    "    }," +
//                    "\"remark\":\"测试金额-哈哈\"," +
//                    "\"business_params\":\"\"," +
////                    "\"business_params\":\"{\\\"payer_show_name\\\":\\\"服务代理\\\"}\"," +
//                    "  }");
            AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
            return response;
//            if(response.isSuccess()){
//                System.out.println("调用成功");
//                return response;
//            } else {
//                System.out.println("调用失败");
//                return null;
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: 组装阿里支付的订单号-H5（wap）
     * @param data - 基本数据
     * @param returnUrl - 成功之后的跳转地址
     * @param notifyUrl - 同步地址
     * @return String
     * @author yoko
     * @date 2019/12/19 19:38
     */
    public static String createH5AlipaySend(String data,String returnUrl, String notifyUrl){
        String resultData;
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_SEND_H5_APP_ID, H5_APP_PRIVATE_KEY, "json", "UTF-8", H5_ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        if (!StringUtils.isBlank(returnUrl)){
            alipayRequest.setReturnUrl(returnUrl);
        }
        alipayRequest.setNotifyUrl(notifyUrl);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(data);//填充业务参数
//        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
//        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
//        alipayRequest.setBizContent("{" +
//                " \"out_trade_no\":\"20150320010101002\"," +
//                " \"total_amount\":\"88.88\"," +
//                " \"subject\":\"Iphone6 16G\"," +
//                " \"product_code\":\"QUICK_WAP_PAY\"" +
//                " }");//填充业务参数
        try {
            resultData = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        return resultData;
    }


    public static void main(String [] args){
//        createAlipaySend(new AlipayModel(), null);
        String returnUrl = "http://www.baidu.com";
        String notifyUrl = "http://114.55.67.167:8083/mg/ali/notify";
        AlipayH5Model alipayH5Model = new AlipayH5Model();
        alipayH5Model.body = "H5支付";
        alipayH5Model.subject = "500-理财";
        alipayH5Model.out_trade_no = "df-hz-hz-order-1";
        alipayH5Model.timeout_express = "30m";
        alipayH5Model.total_amount = "0.01";
        alipayH5Model.product_code = "500-lc";
        String data = JSON.toJSONString(alipayH5Model);
        String resData = createH5AlipaySend(data, returnUrl, notifyUrl);
        System.out.println(data);
        System.out.println(resData);
    }
}
