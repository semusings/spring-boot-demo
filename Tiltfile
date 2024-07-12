docker_compose("./etc/docker/services.yml")
local_resource('npm:install', cmd='npm install', deps=['package.json'])
local_resource('npm:proxy', serve_cmd='npm run proxy', deps=['etc/proxy/config.json'])
local_resource('cy:open', serve_cmd='npm run cy:open')

