docker_compose("./etc/docker/services.yml")
local_resource('npm:install', cmd='npm install --force', deps=['package.json'])
local_resource('openapi:generator', cmd='npm run openapi:generator', deps=['package.json'])
local_resource('npm:proxy', serve_cmd='npm run proxy', deps=['etc/proxy/config.json'])
local_resource('cy:open', serve_cmd='npm run cy:open')

