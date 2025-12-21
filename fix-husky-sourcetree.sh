#!/usr/bin/env bash
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"

if ! command -v node >/dev/null 2>&1; then
  echo " No encuentro 'node' en tu PATH (en Terminal)."
  echo " Si tienes Homebrew: brew install node"
  echo " Si usas nvm: asegúrate de tener node instalado y activo (nvm install --lts && nvm use --lts)"
  exit 1
fi

NODE_PATH="$(command -v node)"
echo " Node encontrado: $NODE_PATH"

# Instala dependencias front (husky vive aquí normalmente)
if [ -f package-lock.json ]; then
  npm ci
elif [ -f package.json ]; then
  npm install
else
  echo " No veo package.json en este repo. Si es monorepo, corre esto dentro del folder del front."
  exit 1
fi

# Crea shim para que SourceTree encuentre 'node' desde node_modules/.bin
mkdir -p node_modules/.bin
cat > node_modules/.bin/node <<EOF
#!/bin/sh
exec "$NODE_PATH" "\$@"
EOF
chmod +x node_modules/.bin/node

echo " Shim creado en node_modules/.bin/node"
echo "Ahora Husky debería poder correr incluso desde SourceTree."
