# CI patch for dual-publish (apply manually — API token lacks workflows scope)

In `.github/workflows/publish.yml` (or equivalent):

```yaml
permissions:
  contents: read
  packages: write
```

Maven job env:
```yaml
GITHUB_ACTOR: ${{ github.actor }}
GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

npm job (GitHub Packages):
```yaml
- uses: actions/setup-node@v4
  with:
    registry-url: https://npm.pkg.github.com
    scope: "@kolektiv"
env:
  NODE_AUTH_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

JSR (after claiming `@kolektiv` on jsr.io):
```yaml
permissions:
  id-token: write
  contents: read
- run: npx jsr publish
```

Org epic: https://github.com/KolektivComputer/.github/issues/2
Reusable workflows (once merged): KolektivComputer/.github
