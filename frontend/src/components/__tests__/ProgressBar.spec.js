import { mount } from '@vue/test-utils'
import ProgressBar from '../ProgressBar.vue'

describe('ProgressBar', () => {
  it('highlights the current step', () => {
    const wrapper = mount(ProgressBar, { props: { currentStep: 2 } })
    const activeSteps = wrapper.findAll('.progress-step.active')
    expect(activeSteps).toHaveLength(1)
    expect(activeSteps[0].find('.circle').text()).toBe('2')
  })
})
