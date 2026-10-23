import os
import sys

import pytest

# Add the project root to the Python path
project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
if project_root not in sys.path:
    sys.path.insert(0, project_root)

from src.service.job_service import JobService


class TestJobService:
    def setup_method(self):
        pass

    def test_create_job_successfully(self):
        # TODO: Complete this test
        pytest.skip("This test has not been implemented yet.")
