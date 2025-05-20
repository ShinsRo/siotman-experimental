from unittest import TestCase


def print_result(origin_charset, to_charset, word):
    print(f"{origin_charset} → {to_charset}: {word}")


class TestWrongCharset(TestCase):
    original = "설정"

    def test_euckr_to_utf8(self):
        # 1. EUC-KR → UTF-8 (깨짐)
        euc = self.original.encode("euc-kr")
        euc_wrong = euc.decode("utf-8", errors="replace")
        print_result("EUC-KR", "UTF-8", euc_wrong)

    def test_utf8_to_euckr(self):
        # 2. UTF-8 → EUC-KR (깨짐)
        utf8 = self.original.encode("utf-8")
        utf_wrong = utf8.decode("euc-kr", errors="replace")
        print_result("UTF-8", "EUC-KR", utf_wrong)

    def test_utf8_to_utf8(self):
        # 5. UTF-8 더블 인코딩
        step1 = self.original.encode("utf-8")
        step2 = step1.decode("latin1")
        step3 = step2.encode("utf-8")
        double_encoded = step3.decode("utf-8", errors="replace")
        print_result("UTF-8", "UTF-8", double_encoded)

    def test_euckr_to_euckr(self):
        # 6. EUC-KR 더블 인코딩
        step1 = self.original.encode("euc-kr")
        step2 = step1.decode("latin1")
        step3 = step2.encode("euc-kr", errors="replace")
        double_encoded = step3.decode("latin1", errors="replace")
        print_result("EUC-KR", "EUC-KR", double_encoded)
