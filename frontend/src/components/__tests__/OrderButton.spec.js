import { mount } from '@vue/test-utils'
import OrderButton from '../OrderButton.vue'

describe('OrderButton', () => {
  it('emits order event on click', async () => {
    const wrapper = mount(OrderButton)
    await wrapper.find('button').trigger('click')
    expect(wrapper.emitted()).toHaveProperty('order')
  })
})
