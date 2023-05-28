#!/usr/bin/env zsh
# Apple M1 Max, Ventura 13.1
# zsh 유틸리티들 한방에 설치하려고 대충 만든 스크립트

. "$HOME/.zprofile"

## Homebrew 설치
echo "### Homebrew 설치 ###"
if ! command -v brew; then
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

  echo "# Set PATH, MANPATH, etc., for Homebrew." >>"$HOME/.zprofile"
  echo "eval '$(/opt/homebrew/bin/brew shellenv)'" >>"$HOME/.zprofile"
  eval "$(/opt/homebrew/bin/brew shellenv)"
fi

echo "버전 : $(brew -v)"
echo "#####################"

## iTerm2 설치
## 키바인딩 프리셋 - "Natural Text Editing" 수동설정 필요
echo "### iTerm2 설치 ###"
if ! (mdfind 'kind:application' -name 'iTerm' | grep 'iTerm.app'); then
  brew install --cask iterm2
fi
echo "#####################"

## zsh 플러그인
echo "### ZSH 플러그인 설치 ###"
if [ ! -d "$HOME/.zsh_plugins" ]; then
  mkdir "$HOME/.zsh_plugins"

  ZSH_PLUGIN_HOME="$HOME/.zsh_plugins"

  function clone_plugin() {
    local owner=$1
    local plugin_name=$2

    if [ ! -d "$ZSH_PLUGIN_HOME/$plugin_name" ]; then
      git clone "https://github.com/$owner/$plugin_name" "$ZSH_PLUGIN_HOME/$plugin_name"
      echo ". $ZSH_PLUGIN_HOME/$plugin_name/$plugin_name.plugin.zsh" >>"$HOME/.zshrc"
    fi
  }

  echo "# zsh 플러그인" >>"$HOME/.zshrc"
  clone_plugin "zsh-users" "zsh-syntax-highlighting"
  clone_plugin "zsh-users" "zsh-autosuggestions"
  clone_plugin "zsh-users" "zsh-completions"
  clone_plugin "zsh-users" "zsh-history-substring-search"

  echo "
  plugins=(
    git
    zsh-syntax-highlighting
    zsh-autosuggestions
    zsh-completions
    zsh-history-substring-search
  )

  " >>"$HOME/.zshrc"
fi
echo "#####################"

## zsh 테마
echo "### ZSH 테마 설치 ###"
if [ ! -d "$HOME/.zsh_theme" ]; then
  mkdir "$HOME/.zsh_theme"
  ZSH_THEME_HOME="$HOME/.zsh_theme"

  ### powerlevel10k 설치
  git clone --depth=1 https://github.com/romkatv/powerlevel10k.git "$ZSH_THEME_HOME/powerlevel10k"
  echo "# zsh 테마" >>"$HOME/.zshrc"
  echo ". $ZSH_THEME_HOME/powerlevel10k/powerlevel10k.zsh-theme" >>"$HOME/.zshrc"

  . "$HOME/.zshrc"
fi
echo "#####################"

## 기타 zsh 설정
echo "# zsh 기타 설정" >>"$HOME/.zshrc"
echo "autoload -Uz compinit && compinit" >>"$HOME/.zshrc"
