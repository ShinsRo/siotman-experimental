import sys

import typer

app = typer.Typer()


@app.command()
def main(
    username: str,
    show_version: bool = typer.Option(prompt="파이썬 버전을 표시할까요?", default=False)
):
    hello: str = f"안녕{f', {username}' if username else ''}. Python 으로부터"
    version: str = f" - python version : {sys.version_info.major}.{sys.version_info.minor}" if show_version else ""

    print(f"{hello}{version}")
